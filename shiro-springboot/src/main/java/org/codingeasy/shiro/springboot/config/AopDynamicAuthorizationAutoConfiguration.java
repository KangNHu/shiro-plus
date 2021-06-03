package org.codingeasy.shiro.springboot.config;

import org.codingeasy.shiro.authorize.interceptor.AopDynamicAuthorizationAdvisor;
import org.codingeasy.shiro.authorize.interceptor.AopDynamicAuthorizationInterceptor;
import org.codingeasy.shiro.authorize.interceptor.DynamicAuthorizationFilter;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
* 基于aop的动态授权自动配置  
* @author : KangNing Hu
*/
@ConditionalOnBean(ShiroPlusAutoConfiguration.class)
public class AopDynamicAuthorizationAutoConfiguration extends AbstractAuthorizationAutoConfiguration{



	@Bean
	public AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor(AuthMetadataManager authMetadataManager){
		AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor = null;
		if (authExceptionHandler == null){
			aopDynamicAuthorizationInterceptor = new AopDynamicAuthorizationInterceptor(authMetadataManager);
		}else {
			aopDynamicAuthorizationInterceptor = new AopDynamicAuthorizationInterceptor(authMetadataManager , this.authExceptionHandler);
		}
		//设置授权处理器
		addAuthorizationHandlers(aopDynamicAuthorizationInterceptor);
		return aopDynamicAuthorizationInterceptor;
	}


	@Bean
	public PointcutAdvisor aopDynamicAuthorizationAdvisor(AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor){
		return new AopDynamicAuthorizationAdvisor(aopDynamicAuthorizationInterceptor);
	}



}
