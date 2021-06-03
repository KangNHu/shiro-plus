package org.codingeasy.shiro.springboot.config;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.context.annotation.Bean;

/**
* 基于aop的授权自动配置 shiro原生的实现加载  
* @author : KangNing Hu
*/
public class AopAuthorizationAutoConfiguration extends AbstractAuthorizationAutoConfiguration {


	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
		return new AuthorizationAttributeSourceAdvisor();
	}
}
