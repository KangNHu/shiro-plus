package org.codingeasy.shiro.springboot;

import org.apache.shiro.util.Assert;
import org.codingeasy.shiro.core.event.EventManager;
import org.codingeasy.shiro.core.metadata.AuthMetadataManager;
import org.codingeasy.shiro.core.metadata.MetadataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
* 基于spring的权限元信息管理器  
* @author : KangNing Hu
*/
public class SpringAuthMetadataManager extends AuthMetadataManager  implements ApplicationListener<ContextRefreshedEvent> {

	private static  final Logger logger = LoggerFactory.getLogger(SpringAuthMetadataManager.class);

	private EventManager eventManager;

	public SpringAuthMetadataManager(MetadataLoader metadataLoader , EventManager eventManager) {
		super(metadataLoader);
		Assert.notNull(eventManager , "事件管理器不能为空");
		this.eventManager = eventManager;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//订阅权限元信息变更事件
		eventManager.register(this);
		logger.info("订阅权限元信息变更事件成功");
	}
}
