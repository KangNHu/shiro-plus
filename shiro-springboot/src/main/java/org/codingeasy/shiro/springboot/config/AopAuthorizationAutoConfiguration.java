package org.codingeasy.shiro.springboot.config;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.codingeasy.shiro.authorize.interceptor.aop.AopAuthorizationAdvisor;
import org.codingeasy.shiro.authorize.interceptor.aop.AopAuthorizationInterceptor;
import org.codingeasy.shiro.authorize.interceptor.aop.AopDynamicAuthorizationAdvisor;
import org.codingeasy.shiro.authorize.interceptor.aop.AopDynamicAuthorizationInterceptor;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
* 基于aop的授权自动配置 shiro原生的实现加载  
* @author : KangNing Hu
*/
@Configuration
@ConditionalOnBean(AuthMetadataManager.class)
public class AopAuthorizationAutoConfiguration extends AbstractAuthorizationAutoConfiguration {



	@Bean(name = "aopAuthorizationInterceptor")
	public AopAuthorizationInterceptor aopAuthorizationInterceptor(AuthMetadataManager authMetadataManager){
		AopAuthorizationInterceptor aopAuthorizationInterceptor = null;
		if (authExceptionHandler == null){
			aopAuthorizationInterceptor = new AopAuthorizationInterceptor(authMetadataManager);
		}else {
			aopAuthorizationInterceptor = new AopAuthorizationInterceptor(authMetadataManager , this.authExceptionHandler);
		}
		//设置授权处理器
		addAuthorizationHandlers(aopAuthorizationInterceptor);
		return aopAuthorizationInterceptor;
	}




	@Bean
	public PointcutAdvisor aopAuthorizationAdvisor(@Qualifier("aopAuthorizationInterceptor") AopAuthorizationInterceptor aopAuthorizationInterceptor){
		return new AopAuthorizationAdvisor(aopAuthorizationInterceptor);
	}
}
