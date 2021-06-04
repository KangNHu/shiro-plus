package org.codingeasy.shiro.springboot.config;

import org.codingeasy.shiro.authorize.interceptor.aop.AopDynamicAuthorizationAdvisor;
import org.codingeasy.shiro.authorize.interceptor.aop.AopDynamicAuthorizationInterceptor;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* 基于aop的动态授权自动配置  
* @author : KangNing Hu
*/
@Configuration
@ConditionalOnBean(AuthMetadataManager.class)
public class AopDynamicAuthorizationAutoConfiguration extends AbstractAuthorizationAutoConfiguration{



	@Bean("aopDynamicAuthorizationInterceptor")
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
	public PointcutAdvisor aopDynamicAuthorizationAdvisor(@Qualifier("aopDynamicAuthorizationInterceptor") AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor){
		return new AopDynamicAuthorizationAdvisor(aopDynamicAuthorizationInterceptor);
	}



}
