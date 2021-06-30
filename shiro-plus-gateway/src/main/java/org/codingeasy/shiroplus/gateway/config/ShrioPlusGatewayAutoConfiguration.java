package org.codingeasy.shiroplus.gateway.config;

import org.apache.shiro.mgt.SecurityManager;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.handler.AuthExceptionHandler;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.mgt.DefaultTenantIdGenerator;
import org.codingeasy.shiroplus.core.mgt.TenantIdGenerator;
import org.codingeasy.shiroplus.gateway.AuthExceptionRedirectHandler;
import org.codingeasy.shiroplus.gateway.TokenGenerator;
import org.codingeasy.shiroplus.gateway.factory.AuthGatewayFilterFactory;
import org.codingeasy.shiroplus.gateway.filter.AuthGatewayFilter;
import org.codingeasy.shiroplus.springboot.config.ShiroPlusSupportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
* gateway自动配置类  
* @author : KangNing Hu
*/
@Import(ShiroPlusSupportConfiguration.class)
public class ShrioPlusGatewayAutoConfiguration {



	@ConditionalOnMissingBean(SecurityManager.class)
	@Bean
	public SecurityManager shiroPlusGatewaySecurityManager(){
		return new ShiroPlusGatewaySecurityManager();
	}





	@Bean
	public GatewayFilterFactory authGatewayFilterFactory(AuthMetadataManager authMetadataManager,
	                                         EventManager eventManager,
                                             @Autowired(required = false) AuthExceptionHandler authExceptionHandler,
	                                         @Autowired(required = false) TokenGenerator tokenGenerator,
	                                         @Autowired(required = false) TenantIdGenerator tenantIdGenerator){
		AuthGatewayFilterFactory authGatewayFilterFactory = new AuthGatewayFilterFactory(authMetadataManager, authExceptionHandler, eventManager);
		authGatewayFilterFactory.setTenantIdGenerator(tenantIdGenerator);
		authGatewayFilterFactory.setTokenGenerator(tokenGenerator);
		return authGatewayFilterFactory;
	}
}
