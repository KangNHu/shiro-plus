package org.codingeasy.shiro.springboot;

import org.apache.shiro.mgt.CachingSecurityManager;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;

/**
* shiro plus上下文初始化器  
* @author : KangNing Hu
*/
public class ShiroPlusInitializer implements ApplicationListener<ApplicationContextEvent> {

	private static  final Logger  logger = LoggerFactory.getLogger(ShiroPlusInitializer.class);

	private CachingSecurityManager cachingSecurityManager;

	private AuthMetadataManager authMetadataManager;

	public ShiroPlusInitializer(CachingSecurityManager cachingSecurityManager , AuthMetadataManager authMetadataManager){
		this.cachingSecurityManager = cachingSecurityManager;
		this.authMetadataManager = authMetadataManager;
	}


	public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
		//订阅权限元信息变更事件
		cachingSecurityManager.getEventBus().register(authMetadataManager);
		logger.info("订阅权限元信息变更事件成功");
	}
}
