package org.codingeasy.shiroplus.springboot.config;

import org.aopalliance.intercept.MethodInvocation;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.interceptor.aop.AopAuthorizationAdvisor;
import org.codingeasy.shiroplus.core.interceptor.aop.AopAuthorizationInterceptor;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
* 基于aop的授权自动配置 shiro原生的实现加载  
* @author : kangning <a>2035711178@qq.com</a>
*/
@ConditionalOnBean({AuthMetadataManager.class , EventManager.class})
public class AopAuthorizationAutoConfiguration extends AbstractAuthorizationAutoConfiguration<MethodInvocation, MethodInvocation> {



	@Bean(name = "aopAuthorizationInterceptor")
	public AopAuthorizationInterceptor aopAuthorizationInterceptor(AuthMetadataManager authMetadataManager ,
	                                                               EventManager eventManager){
		AopAuthorizationInterceptor aopAuthorizationInterceptor = new AopAuthorizationInterceptor(authMetadataManager ,eventManager);
		//设置授权处理器
		setCommonComponent(aopAuthorizationInterceptor);
		return aopAuthorizationInterceptor;
	}




	@Bean
	public PointcutAdvisor aopAuthorizationAdvisor(@Qualifier("aopAuthorizationInterceptor") AopAuthorizationInterceptor aopAuthorizationInterceptor){
		return new AopAuthorizationAdvisor(aopAuthorizationInterceptor);
	}
}
