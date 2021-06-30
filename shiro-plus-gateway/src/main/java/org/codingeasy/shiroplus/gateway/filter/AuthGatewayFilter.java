package org.codingeasy.shiroplus.gateway.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.handler.AuthExceptionHandler;
import org.codingeasy.shiroplus.core.interceptor.AbstractAuthorizationInterceptor;
import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.utils.PathUtils;
import org.codingeasy.shiroplus.gateway.GatewayAuthExceptionHandler;
import org.codingeasy.shiroplus.gateway.GatewayInvoker;
import org.codingeasy.shiroplus.gateway.TokenGenerator;
import org.codingeasy.shiroplus.gateway.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
* 认证网关过滤器  
* @author : KangNing Hu
*/
public class AuthGatewayFilter extends AbstractAuthorizationInterceptor implements GatewayFilter {


	private final static Logger logger = LoggerFactory.getLogger(AuthGatewayFilter.class);

	private AuthExceptionHandler authExceptionHandler;

	private TokenGenerator tokenGenerator;

	private String pathPrefix = "";

	public AuthGatewayFilter(AuthMetadataManager authMetadataManager, AuthExceptionHandler authExceptionHandler, EventManager eventManager) {
		super(authMetadataManager, authExceptionHandler, eventManager);
		this.authExceptionHandler = authExceptionHandler;
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	public void setTokenGenerator(TokenGenerator tokenGenerator) {
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		GatewayInvoker gatewayInvoker = createGatewayInvoker(exchange , chain);
		ServerHttpRequest request = exchange.getRequest();
		//是否开启鉴权
		String tenantId = getTenantId(gatewayInvoker);
		GlobalMetadata globalMetadata = authMetadataManager.getGlobalMetadata(tenantId);
		if (globalMetadata == null){
			logger.warn("当前请求 url[{}] headers [{}] 没有申请租户"  ,
					request.getPath().pathWithinApplication().value(),
					request.getHeaders());
			return WebUtils.write("当前请求所属服务没有申请租户" , exchange.getResponse() , HttpStatus.FORBIDDEN);
		}
		Boolean enableAuthentication = globalMetadata.getEnableAuthentication();
		if (enableAuthentication != null&& !enableAuthentication){
			return chain.filter(exchange);
		}
		//是否忽略url 白名单
		RequestPath path = request.getPath();
		String url = path.pathWithinApplication().value();
		if (PathUtils.matches(globalMetadata.getAnons() , url)) {
			return chain.filter(exchange);
		}
		//鉴权
		Subject subject = SecurityUtils.getSubject();
		try {
			AuthenticationToken authenticationToken = tokenGenerator.generate(exchange.getRequest());
			if (authenticationToken == null){
				throw new AuthenticationException("Invalid certificate");
			}
			subject.login(authenticationToken);
		}catch (AuthenticationException e){
			authExceptionHandler.authenticationFailure(gatewayInvoker , e);
		}
		//获取异常处理结果
		Mono<Void> exceptionHandlerResult = getExceptionHandlerResult();
		//授权
		return exceptionHandlerResult == null ?(Mono<Void>) invoke(gatewayInvoker) : exceptionHandlerResult;
	}


	@Override
	protected Object authExceptionAfterProcessor(AuthExceptionHandler authExceptionHandler) {
			return getExceptionHandlerResult();
	}


	/**
	 * 获取异常处理结果
	 * @return 返回处理结果，如果没有则返回null
	 */
	private Mono<Void> getExceptionHandlerResult(){
		if (authExceptionHandler instanceof GatewayAuthExceptionHandler){
			Mono<Void> result = ((GatewayAuthExceptionHandler) authExceptionHandler).getResult();
			if (result != null){
				return result;
			}
		}
		return null;
	}

	/**
	 * 创建调用器
	 * @param exchange server web 上下文
	 * @param chain 过滤链
	 * @return 返回调用器
	 */
	private GatewayInvoker createGatewayInvoker(ServerWebExchange exchange, GatewayFilterChain chain) {
		return new GatewayInvoker(exchange, chain , this.pathPrefix);
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
