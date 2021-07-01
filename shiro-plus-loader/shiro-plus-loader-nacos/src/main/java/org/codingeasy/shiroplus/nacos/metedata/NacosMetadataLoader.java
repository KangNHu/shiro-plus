package org.codingeasy.shiroplus.nacos.metedata;

import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.alibaba.nacos.spring.context.event.config.EventPublishingConfigService;
import com.alibaba.nacos.spring.util.ConfigParseUtils;
import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.AbstractMetadata;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.MetadataLoader;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.nacos.configuration.ShiroPlusNacosProperties;
import org.codingeasy.shiroplus.nacos.excption.NacosMetadataLoaderException;
import org.codingeasy.shiroplus.nacos.parse.ConfigParse;
import org.codingeasy.shiroplus.nacos.parse.ConfigParseDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 基于nacos的元数据加载器
 *
 * @author : kangning <a>2035711178@qq.com</a>
 */
public class NacosMetadataLoader implements MetadataLoader, ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(NacosMetadataLoader.class);

	private static final String PERMISSION_DATA_ID = "org.codingeasy.shiroplus.permission.metadata";

	private static final String GLOBAL_DATA_ID = "org.codingeasy.shiroplus.global.metadata";

	private static final String GROUP = "SHIR_PLUS_METADATA";

	private final static long TIMEOUT = 50000;

	private ConfigService configService;

	private ShiroPlusNacosProperties shiroPlusNacosProperties;

	private EventManager eventManager;

	private ConfigParseDelegate configParseDelegate = new ConfigParseDelegate();


	public NacosMetadataLoader(ConfigService configService,
	                           ShiroPlusNacosProperties properties,
	                           EventManager eventManager) {
		this.configService = configService;
		this.shiroPlusNacosProperties = properties;
		this.eventManager = eventManager;
	}


	/**
	 * 添加配置解析器
	 *
	 * @param configParse 配置解析器对象
	 */
	public void addConfigParse(ConfigParse configParse) {
		this.configParseDelegate.addConfigParse(configParse);
	}


	@Override
	public List<PermissionMetadata> load() {
		return getMetadataList(PERMISSION_DATA_ID, NacosPermissionMetadata.class);
	}


	@Override
	public List<GlobalMetadata> loadGlobal() {
		return getMetadataList(GLOBAL_DATA_ID, NacosGlobalMetadata.class);
	}


	/**
	 * 获取元数据列表
	 *
	 * @param dataId 数据id
	 * @param tClass class
	 * @return
	 */
	protected List getMetadataList(String dataId, Class tClass) {
		try {
			String config = configService.getConfig(dataId, getGroup(), getTimeout());
			return configParseDelegate.parse(
					config,
					tClass,
					shiroPlusNacosProperties.getConfigType()
			);
		} catch (Exception e) {
			throw new NacosMetadataLoaderException(e);
		}
	}

	/**
	 * 获取超时时间
	 *
	 * @return 返回超时时间 单位毫秒
	 */
	private long getTimeout() {
		return shiroPlusNacosProperties.getTimeout() == -1 ? TIMEOUT : shiroPlusNacosProperties.getTimeout();
	}


	/**
	 * 获取nacos 分组
	 * @return 返回分组
	 */
	private String getGroup(){
		return StringUtils.isEmpty(shiroPlusNacosProperties.getGroup()) ? GROUP : shiroPlusNacosProperties.getGroup();
	}

	// add event listener
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		try {
			//注册permission metadata变更事件处理
			configService.addListener(PERMISSION_DATA_ID, GROUP, new Listener() {
				@Override
				public Executor getExecutor() {
					return eventManager.getExecutor();
				}

				@Override
				public void receiveConfigInfo(String configInfo) {
					sendMetadataUpdateEvent(configInfo , NacosPermissionMetadata.class);
				}
			});
			logger.info("permission metadata update event listener");
			//注册 global metadata变更事件
			configService.addListener(GLOBAL_DATA_ID, GROUP, new Listener() {
				@Override
				public Executor getExecutor() {
					return eventManager.getExecutor();
				}

				@Override
				public void receiveConfigInfo(String configInfo) {
					sendMetadataUpdateEvent(configInfo , NacosGlobalMetadata.class);
				}
			});
			logger.info("global metadata update event listener");
		} catch (NacosException e) {
			throw new NacosMetadataLoaderException(e);
		}
	}

	/**
	 * 发送事件
	 * @param configInfo nacos事件源
	 * @param clazz 配置信息的类型
	 */
	private void sendMetadataUpdateEvent(String configInfo , Class<? extends NacosMetadata> clazz){
		//解析变更配置信息
		List<? extends NacosMetadata> nacosMetadataList = configParseDelegate.parse(
				configInfo,
				clazz,
				shiroPlusNacosProperties.getConfigType(),
				true
		);
		//事件分组
		Map<AuthMetadataEvent.EventType, ? extends List<? extends NacosMetadata>> group = nacosMetadataList
				.stream()
				.collect(
						Collectors.groupingBy(NacosMetadata::getEventType)
				);
		//事件发送
		for (Map.Entry<AuthMetadataEvent.EventType, ? extends List<? extends NacosMetadata>> entry : group.entrySet()){
			eventManager.publish(new AuthMetadataEvent(entry.getKey() , entry.getValue()));
		}
	}



	private static class MetadataListener extends AbstractConfigChangeListener{

		@Override
		public void receiveConfigChange(ConfigChangeEvent event) {

		}


	}
}
