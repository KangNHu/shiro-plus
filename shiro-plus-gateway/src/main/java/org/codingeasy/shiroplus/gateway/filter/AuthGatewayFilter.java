package org.codingeasy.shiroplus.gateway.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.handler.AuthExceptionHandler;
import org.codingeasy.shiroplus.core.interceptor.AbstractAuthorizationInterceptor;
import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.interceptor.WebInvoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.utils.PathUtils;
import org.codingeasy.shiroplus.gateway.GatewayInvoker;
import org.codingeasy.shiroplus.gateway.HeadTokenGenerator;
import org.codingeasy.shiroplus.gateway.TokenGenerator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
* 认证网关过滤器  
* @author : KangNing Hu
*/
public class AuthGatewayFilter extends AbstractAuthorizationInterceptor implements GlobalFilter {

	private AuthExceptionHandler authExceptionHandler;

	private TokenGenerator tokenGenerator = new HeadTokenGenerator();

	public AuthGatewayFilter(AuthMetadataManager authMetadataManager, AuthExceptionHandler authExceptionHandler, EventManager eventManager) {
		super(authMetadataManager, authExceptionHandler, eventManager);
		this.authExceptionHandler = authExceptionHandler;
	}


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		GatewayInvoker gatewayInvoker = new GatewayInvoker(exchange, chain);
		ServerHttpRequest request = exchange.getRequest();
		//是否开启鉴权
		String tenantId = getTenantId(gatewayInvoker);
		GlobalMetadata globalMetadata = authMetadataManager.getGlobalMetadata(tenantId);
		if (globalMetadata == null){
			throw new IllegalStateException(String.format(
					"当前请求 url[%s] headers [%s] 没有申请租户" ,
					request.getPath().pathWithinApplication().value(),
					request.getHeaders()
					)
			);
		}
		Boolean enableAuthentication = globalMetadata.getEnableAuthentication();
		if (enableAuthentication != null&& !enableAuthentication){
			return Mono.empty();
		}
		//是否忽略url 白名单
		RequestPath path = request.getPath();
		String url = path.pathWithinApplication().value();
		if (PathUtils.matches(globalMetadata.getAnons() , url)) {
			return Mono.empty();
		}
		//鉴权
		SecurityManager securityManager = SecurityUtils.getSecurityManager();
		try {
			AuthenticationToken authenticationToken = tokenGenerator.generate(exchange.getRequest());
			if (authenticationToken == null){
				throw new AuthenticationException("Invalid certificate");
			}
			securityManager.authenticate(authenticationToken);
		}catch (AuthenticationException e){
			authExceptionHandler.authenticationFailure(gatewayInvoker , e);
		}
		//授权
		return (Mono<Void>) invoke(gatewayInvoker);
	}



	/**
	 * 获取租户id
	 * @param invoker 调用器
	 * @return 返回租户id
	 */
	private String getTenantId(Invoker invoker) {
		String tenantId = this.tenantIdGenerator.generate(invoker);
		return tenantId == null ? this.tenantIdGenerator.getDefault() : tenantId;
	}


	/**
	 * 是否开启授权
	 * @return 如果开启返回true否则false
	 */
	@Override
	protected boolean isEnableAuthorization(Invoker invoker) {
		String tenantId = this.tenantIdGenerator.generate(invoker);
		GlobalMetadata globalMetadata = this.authMetadataManager.getGlobalMetadata(tenantId);
		if (globalMetadata == null){
			return true;
		}
		//处理总开关
		return globalMetadata.getEnableAuthorization() == null || globalMetadata.getEnableAuthorization();
	}
}
