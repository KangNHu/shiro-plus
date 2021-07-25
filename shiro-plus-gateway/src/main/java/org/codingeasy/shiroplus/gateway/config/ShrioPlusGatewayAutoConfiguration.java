package org.codingeasy.shiroplus.gateway.config;

import org.apache.shiro.mgt.SecurityManager;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.realm.CommonAuthRealm;
import org.codingeasy.shiroplus.core.realm.processor.AuthProcessor;
import org.codingeasy.shiroplus.gateway.HttpGatewayAuthProcessor;
import org.codingeasy.shiroplus.gateway.factory.AuthGatewayFilterFactory;
import org.codingeasy.shiroplus.springboot.config.ShiroPlusSupportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* gateway自动配置类  
* @author : KangNing Hu
*/
@ConditionalOnProperty(
		prefix ="shiroplus.gateway",
		name = "enable",
		havingValue = "true"
)
@Import(ShiroPlusSupportConfiguration.class)
public class ShrioPlusGatewayAutoConfiguration {



	@ConditionalOnMissingBean(SecurityManager.class)
	@Bean
	public SecurityManager shiroPlusGatewaySecurityManager(){
		return new ShiroPlusGatewaySecurityManager();
	}



	/**
	 * 注册通用的realm
	 * @param authProcessor
	 * @return
	 */
	@Bean
	public CommonAuthRealm<ServerHttpRequest, ServerHttpResponse> authServletRealm(@Autowired(required = false) AuthProcessor<ServerHttpRequest, ServerHttpResponse> authProcessor){
		return new CommonAuthRealm<>(authProcessor);
	}



	@Bean
	public GatewayFilterFactory authGatewayFilterFactory(AuthMetadataManager authMetadataManager,
	                                         EventManager eventManager,
	                                         @Autowired(required = false) HttpGatewayAuthProcessor authProcessor){
		return new AuthGatewayFilterFactory(authMetadataManager, authProcessor , eventManager);
	}

}
