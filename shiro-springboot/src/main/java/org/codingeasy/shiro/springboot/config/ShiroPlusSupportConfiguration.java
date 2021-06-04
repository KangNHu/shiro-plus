package org.codingeasy.shiro.springboot.config;

import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.springboot.AuthMetadataEventPublisher;
import org.codingeasy.shiro.springboot.ShiroPlusInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
* shiro plus的辅助配置类  
* @author : KangNing Hu
*/
public class ShiroPlusSupportConfiguration {


	/**
	 * 注册一个权限元信息事件的发布器
	 * @param cachingSecurityManager 元信息事件的发布器
	 * @return
	 */
	@Bean
	public AuthMetadataEventPublisher eventBus(SecurityManager cachingSecurityManager){
		return new AuthMetadataEventPublisher((CachingSecurityManager) cachingSecurityManager);
	}


	/**
	 * 注册shiroPlus 初始化器
	 * @return
	 */
	@Bean
	public ShiroPlusInitializer shiroPlusInitializer(SecurityManager cachingSecurityManager,
	                                                 AuthMetadataManager authMetadataManager){
		return new ShiroPlusInitializer((CachingSecurityManager) cachingSecurityManager, authMetadataManager);
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
