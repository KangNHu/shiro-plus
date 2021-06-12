package org.codingeasy.shiroplus.springboot.config;

import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.MetadataLoader;
import org.codingeasy.shiroplus.springboot.SpringAuthMetadataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
* shiroplus plus的辅助配置类
* @author : kangning <a>2035711178@qq.com</a>
*/
public class ShiroPlusSupportConfiguration {


	/**
	 * 注册一个事件管理器
	 * @param cachingSecurityManager shiroplus 安全管理器
	 * @return
	 */
	@Bean
	public EventManager eventManager(SecurityManager cachingSecurityManager){
		return new EventManager(((CachingSecurityManager) cachingSecurityManager).getEventBus());
	}

	/**
	 * 注册一个权限元信息管理器
	 * @return
	 */
	@Bean
	public AuthMetadataManager authMetadataManager(@Autowired(required = false) MetadataLoader metadataLoader,
	                                               EventManager eventManager){
		return new SpringAuthMetadataManager(metadataLoader ,eventManager );
	}

	/**
	 * 注册事件自动注册器
	 * @param eventManager
	 * @return
	 */
	@Bean
	public EventListenerAutoRegistry eventListenerAnnotationBeanPostProcessor(EventManager eventManager){
		return new EventListenerAutoRegistry(eventManager);
	}


	/**
	 * 注册shiro bean的生命周期处理器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(LifecycleBeanPostProcessor.class)
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
}
