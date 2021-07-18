package org.codingeasy.shiroplus.springboot.config;

import org.aopalliance.intercept.MethodInvocation;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.interceptor.aop.AopDynamicAuthorizationAdvisor;
import org.codingeasy.shiroplus.core.interceptor.aop.AopDynamicAuthorizationInterceptor;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* 基于aop的动态授权自动配置  
* @author : kangning <a>2035711178@qq.com</a>
*/
@ConditionalOnBean({AuthMetadataManager.class , EventManager.class})
public class AopDynamicAuthorizationAutoConfiguration extends AbstractAuthorizationAutoConfiguration<MethodInvocation, MethodInvocation>{



	@Bean("aopDynamicAuthorizationInterceptor")
	public AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor(AuthMetadataManager authMetadataManager ,
	                                                                             EventManager eventManager){
		AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor = new AopDynamicAuthorizationInterceptor(authMetadataManager , eventManager);
		//设置通用组件
		setCommonComponent(aopDynamicAuthorizationInterceptor);
		return aopDynamicAuthorizationInterceptor;
	}


	@Bean
	public PointcutAdvisor aopDynamicAuthorizationAdvisor(@Qualifier("aopDynamicAuthorizationInterceptor") AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor){
		return new AopDynamicAuthorizationAdvisor(aopDynamicAuthorizationInterceptor);
	}



}
