package org.codingeasy.shiroplus.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.interceptor.AbstractAuthorizationInterceptor;
import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.MetadataContext;
import org.codingeasy.shiroplus.core.realm.RequestToken;
import org.codingeasy.shiroplus.core.realm.processor.AuthProcessor;
import org.codingeasy.shiroplus.core.utils.PathUtils;
import org.codingeasy.shiroplus.gateway.GatewayInvoker;
import org.codingeasy.shiroplus.gateway.HttpGatewayAuthProcessor;
import org.codingeasy.shiroplus.gateway.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
* 认证网关过滤器  
* @author : KangNing Hu
*/
public class AuthGatewayFilter extends AbstractAuthorizationInterceptor<ServerHttpRequest , ServerHttpResponse> implements GatewayFilter {


	private final static Logger logger = LoggerFactory.getLogger(AuthGatewayFilter.class);

	private String pathPrefix = "";

	public AuthGatewayFilter(AuthMetadataManager authMetadataManager, AuthProcessor<ServerHttpRequest ,ServerHttpResponse> authProcessor, EventManager eventManager) {
		super(authMetadataManager, eventManager);
		setAuthProcessor(authProcessor);
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}


	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			GatewayInvoker gatewayInvoker = createGatewayInvoker(exchange, chain);
			ServerHttpRequest request = exchange.getRequest();
			//是否开启鉴权
			GlobalMetadata globalMetadata = super.getGlobalMetadata(request);
			if (globalMetadata == null) {
				logger.warn("当前请求 url[{}] headers [{}] 没有申请租户",
						request.getPath().pathWithinApplication().value(),
						request.getHeaders());
				return WebUtils.write("当前请求所属服务没有申请租户", exchange.getResponse(), HttpStatus.FORBIDDEN);
			}
			Boolean enableAuthentication = globalMetadata.getEnableAuthentication();
			if (enableAuthentication != null && !enableAuthentication) {
				return chain.filter(exchange);
			}
			//是否忽略url 白名单
			RequestPath path = request.getPath();
			String url = path.pathWithinApplication().value();
			if (PathUtils.matches(globalMetadata.getAnons(), url)) {
				return chain.filter(exchange);
			}
			//获取token
			String token = this.authProcessor.getToken(exchange.getRequest());
			if (StringUtils.isEmpty(token)) {
				throw new AuthenticationException("Invalid certificate");
			}
			//鉴权
			Subject subject = SecurityUtils.getSubject();
			try {
				subject.login(new RequestToken<>(exchange.getRequest(), token));
			} catch (AuthenticationException e) {
				authProcessor.authenticationFailure(gatewayInvoker.getRequest(), gatewayInvoker.getResponse(), e);
			}
			//获取异常处理结果
			Mono<Void> exceptionHandlerResult = getExceptionHandlerResult();
			if (exceptionHandlerResult != null) {
				return exceptionHandlerResult;
			}
			//超级管理员校验
			Object adminId = globalMetadata.get(GlobalMetadata.EXTEND_ADMIN_ID_KEY);
			if (adminId != null && adminId.equals(subject.getPrincipal())) {
				return chain.filter(exchange);
			}
			//授权
			return (Mono<Void>) invoke(gatewayInvoker);
		}finally {
			MetadataContext.remove();
		}
	}


	@Override
	protected void clear() {
		///
	}

	@Override
	protected Object authExceptionAfterProcessor(Invoker<ServerHttpRequest, ServerHttpResponse> invoker, AuthorizationException e) {
		return getExceptionHandlerResult();
	}

	/**
	 * 获取异常处理结果
	 * @return 返回处理结果，如果没有则返回null
	 */
	private Mono<Void> getExceptionHandlerResult(){
		if (authProcessor instanceof HttpGatewayAuthProcessor){
			return ((HttpGatewayAuthProcessor) authProcessor).getResult();
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
	 * 是否开启授权
	 * @return 如果开启返回true否则false
	 */
	@Override
	protected boolean isEnableAuthorization(Invoker<ServerHttpRequest , ServerHttpResponse> invoker) {
		GlobalMetadata globalMetadata = super.getGlobalMetadata(invoker.getRequest());
		if (globalMetadata == null){
			return true;
		}
		//处理总开关
		return globalMetadata.getEnableAuthorization() == null || globalMetadata.getEnableAuthorization();
	}

	/**
	 * 获取权限元数据key
	 * @return 返回key path + ":" + method 其中path 由于是网关可能会经过特殊处理
	 */
	@Override
	protected String getPermissionMetadataKey(ServerHttpRequest request) {
		RequestPath path = request.getPath();
		return StringUtils.substringAfter(path.pathWithinApplication().value() , this.pathPrefix) + ":" + request.getMethod();
	}
}
