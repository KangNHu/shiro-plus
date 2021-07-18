package org.codingeasy.shiroplus.loader.admin.client.metadata;

import org.apache.commons.collections.CollectionUtils;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.MetadataLoader;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.client.AdminClient;
import org.codingeasy.shiroplus.loader.admin.client.configuration.AdminServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* shiro plus admin的加载器  
* @author : KangNing Hu
*/
public class ShiroPlusAdminClientMetadataLoader implements Runnable ,  MetadataLoader , ApplicationListener<ContextRefreshedEvent> {

	private final static Logger logger = LoggerFactory.getLogger(ShiroPlusAdminClientMetadataLoader.class);

	private EventManager eventManager;

	private AdminClient adminClient;

	private long refreshInterval = 3 * 1000;

	private volatile boolean isStart = false;

	public ShiroPlusAdminClientMetadataLoader(EventManager eventManager , AdminClient adminClient){
		this.adminClient = adminClient;
		this.eventManager = eventManager;
	}

	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	@Override
	public List<PermissionMetadata> load() {
		return adminClient.getPermissionConfigs().getData();
	}

	@Override
	public List<GlobalMetadata> loadGlobal() {
		return adminClient.getGlobalConfigs().getData();
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (isStart){
			logger.info("元数据同步已启动");
			return;
		}
		this.eventManager.getExecutor().execute(this::run);
		this.isStart = true;
	}

	@Override
	public void run() {
		try {
			List<AuthMetadataEvent> events = this.adminClient.pullEvents().getData();
			if (CollectionUtils.isEmpty(events)){
				if (logger.isDebugEnabled()){
					logger.debug("未发生变更事件");
				}
				return;
			}
			//循环发送事件
			events.stream().forEach(this.eventManager::publish);
			//休眠
			TimeUnit.MILLISECONDS.sleep(refreshInterval);
		}catch (Exception e){
			logger.error("处理元数据变更失败" ,e);
		}finally {
			this.eventManager.getExecutor().execute(this::run);
		}
	}
}
