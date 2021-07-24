package org.codingeasy.shiroplus.loader.admin.client;


import com.hazelcast.util.MD5Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.util.Assert;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.loader.admin.client.configuration.AdminServerProperties;
import org.codingeasy.shiroplus.loader.admin.client.exception.ServerDownException;
import org.codingeasy.shiroplus.loader.admin.client.exception.ServerUnregisterException;
import org.codingeasy.shiroplus.loader.admin.client.model.AuthMetadataEventWrap;
import org.codingeasy.shiroplus.loader.admin.client.model.GlobalMetadataEventDto;
import org.codingeasy.shiroplus.loader.admin.client.model.PermissionMetadataEventDto;
import org.codingeasy.shiroplus.loader.admin.client.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
* 通信工作 负责和服务端交互  
* @author : KangNing Hu
*/
public class CommunicationWork implements ApplicationListener<ContextRefreshedEvent> , EnvironmentAware {


	private static final Logger logger = LoggerFactory.getLogger(CommunicationWork.class);

	//ping 的间隔 3 s
	private final static long PING_INTERVAL = 3000;

	/**
	 * 工作的线程池
	 */
	private EventManager eventManager;

	/**
	 * admin客户端
	 */
	private AdminClient adminClient;

	/**
	 * 配置
	 */
	private AdminServerProperties adminServerProperties;


	/**
	 * 应用名称
	 * <p>取至 spring.application.name配置</p>
	 */
	private String applicationName;

	/**
	 * 任务状态
	 */
	private volatile boolean state = false;
	/**
	 * 实例编码
	 */
	private String instanceCode;

	/**
	 * 元信息同步错误此时
	 */
	private int errorCount = 1;

	/**
	 * ping 错误次数
	 */
	private int pingErrorCount =1;

	public CommunicationWork(EventManager eventManager ,AdminClient adminClient ,AdminServerProperties adminServerProperties){
		Assert.notNull(eventManager , "eventManager is not null");
		Assert.notNull(adminClient , "adminClient is not null");
		Assert.notNull(adminServerProperties ,"adminServerProperties is not null");
		this.eventManager = eventManager;
		this.adminClient = adminClient;
		this.adminServerProperties = adminServerProperties;
	}


	/**
	 * 开启任务
	 */
	public void start(){
		Executor executor = eventManager.getExecutor();
		//创建实例
		registerInstance();
		this.state = true;
		//启动心跳
		executor.execute(new pingRunnable());
		//启动元数据同步
		executor.execute(new MetadataChangeRunnable());
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (state){
			logger.info("元数据同步已启动");
			return;
		}
		start();
	}

	/**
	 * 注册实例
	 */
	private void registerInstance() {
		this.instanceCode = MD5Util.toMD5String(UUID.randomUUID().toString().replace("-" ,""));
		adminClient.createInstance(instanceCode , applicationName);
		logger.info("注册实例成功 code:{}" , instanceCode);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.applicationName = environment.getProperty("spring.application.name");
	}

	/**
	 * 休眠
	 * @param timeout 休眠事件
	 */
	private void sleep(long timeout){
		//休眠
		try {
			TimeUnit.MILLISECONDS.sleep(timeout);
		} catch (InterruptedException e) {
			///
		}
	}

	/**
	 * 元数据同步任务
	 */
	private class MetadataChangeRunnable implements Runnable{

		@Override
		public void run() {
			if (!state){
				return;
			}
			try {
				AuthMetadataEventWrap authMetadataEventWrap = adminClient.pullEvents(instanceCode).getData();
				List<GlobalMetadataEventDto> globalMetadataEvents = authMetadataEventWrap.getGlobalMetadataEvents();
				List<PermissionMetadataEventDto> permissionMetadataEvents = authMetadataEventWrap.getPermissionMetadataEvents();
				if (CollectionUtils.isEmpty(globalMetadataEvents) && CollectionUtils.isEmpty(permissionMetadataEvents)){
					if (logger.isDebugEnabled()){
						logger.debug("未发生变更事件");
					}
					return;
				}
				//循环发送事件
				Arrays.asList(
						Optional.ofNullable(globalMetadataEvents).orElse(new ArrayList<>()),
						Optional.ofNullable(permissionMetadataEvents).orElse(new ArrayList<>())
				).stream().flatMap(List::stream).forEach(eventManager::publish);
				//重置错误次数
				errorCount = 1;
			}catch (Exception e){
				//增加错误次数
				errorCount++;
				logger.error("处理元数据变更失败" ,e);
			}finally {
				//休眠
				sleep(adminServerProperties.getRefreshInterval() * errorCount);
				eventManager.getExecutor().execute(this::run);
			}
		}
	}

	/**
	 * 心跳任务
	 */
	private class pingRunnable implements Runnable{

		@Override
		public void run() {
			if (pingErrorCount > adminServerProperties.getPingMaxFailureCount()){
				state = false;
				throw new ServerDownException("心跳超时,请确定admin server是否存在");
			}
			boolean execute = true;
			try {
				Response<Integer> ping = adminClient.ping(instanceCode);
				if (!ping.isSucceed()){
					throw new IllegalStateException("心跳失败");
				}
				if (ping.getData() == 0){
					throw new ServerUnregisterException("服务为注册");
				}
				//重置错误次数
				pingErrorCount = 1;
				if (logger.isDebugEnabled()){
					logger.debug("ping admin server succeed");
				}
			}catch (ServerUnregisterException e){
				state = false;
				execute= false;
				throw e;
			}catch (Exception e){
				logger.warn("心跳异常" ,e);
				pingErrorCount ++;
			}finally {
				if (execute) {
					sleep(PING_INTERVAL);
					eventManager.getExecutor().execute(this::run);
				}
			}
		}
	}
}
