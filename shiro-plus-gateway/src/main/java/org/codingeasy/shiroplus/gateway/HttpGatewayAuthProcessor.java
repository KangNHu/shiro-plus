package org.codingeasy.shiroplus.gateway;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.MetadataContext;
import org.codingeasy.shiroplus.core.realm.processor.DefaultAuthProcessor;
import org.codingeasy.shiroplus.gateway.factory.AuthGatewayFilterFactory;
import org.codingeasy.shiroplus.gateway.token.SimpleGatewayAuthenticationToken;
import org.codingeasy.shiroplus.gateway.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
* 基于web flux 的网关 权限处理器  
* @author : KangNing Hu
*/
public class HttpGatewayAuthProcessor extends DefaultAuthProcessor<ServerHttpRequest , ServerHttpResponse>  {

	private final  static Logger logger = LoggerFactory.getLogger(HttpGatewayAuthProcessor.class);
	private static final String UNAUTHORIZED_URL = "unauthorized-url";
	private static final String UNAUTHENTICATED_URL = "unauthenticated-url";

	private ThreadLocal<Mono<Void>> redirectMono = new ThreadLocal<>();

	private AuthGatewayFilterFactory.Config config;

	public HttpGatewayAuthProcessor(AuthGatewayFilterFactory.Config config){
		this.config = config;
	}

	public HttpGatewayAuthProcessor() {
	}

	@Override
	public String getToken(ServerHttpRequest request) {
		String token;
		String tokenName = config.getTokenName();
		switch (config.getTokenStrategy()){
			case HEAD:
				token = request.getHeaders().getFirst(tokenName);
				break;
			case COOKIE:
				HttpCookie tenantHttpCookie = request.getCookies().getFirst(tokenName);
				token = tenantHttpCookie != null ? tenantHttpCookie.getValue() : null;
				break;
			case QUERY:
			case PATH:
			default:
				token = request.getQueryParams().getFirst(tokenName);
		}
		return token;
	}

	/**
	 * 动态从网关中获取当前请求的租户id
	 * @param request 请求对象
	 * @return
	 */
	@Override
	public String getTenantId(ServerHttpRequest request) {
		String tenantId = null;
		String tenantName = config.getTenantName();
		int tenantPathIndex = config.getTenantPathIndex();
		switch (config.getTenantStrategy()){
			case HEAD:
				tenantId = request.getHeaders().getFirst(tenantName);
				break;
			case QUERY:
				tenantId = request.getQueryParams().getFirst(tenantName);
				break;
			case COOKIE:
				HttpCookie tenantHttpCookie = request.getCookies().getFirst(tenantName);
				tenantId = tenantHttpCookie != null ? tenantHttpCookie.getValue() : null;
				break;
			case PATH:
			default:
				String path = request.getPath().pathWithinApplication().value();
				String[] split = path.split("/");
				if (split.length >= tenantPathIndex){
					tenantId = split[tenantPathIndex + 1];
				}

		}
		return tenantId == null ? super.getDefaultTenantId() : tenantId;
	}





	/**
	 * 授权失败处理
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	@Override
	public void authorizationFailure(ServerHttpRequest request, ServerHttpResponse response, AuthorizationException e) {
		redirectMono.set(doProcessor(request , response  , UNAUTHORIZED_URL ,
				HttpStatus.UNAUTHORIZED , e.getMessage()));
	}


	/**
	 * 鉴权失败处理
	 * @param request 请求对象
	 * @param response  响应对象
	 */
	@Override
	public void authenticationFailure(ServerHttpRequest request, ServerHttpResponse response, AuthenticationException e) {
		redirectMono.set(doProcessor(request , response  , UNAUTHENTICATED_URL ,
				HttpStatus.FORBIDDEN , e.getMessage()));
	}




	/**
	 * 获取重定向结果
	 * @return 返回 redirect mono
	 */
	public Mono<Void> getResult(){
		try {
			return redirectMono.get();
		}finally {
			redirectMono.remove();
		}
	}

	/**
	 * 执行重定向
	 * @param response 响应对象
	 * @param request 请求对象
	 * @param redirectAttrName 在{@link GlobalMetadata}中重定向属性名称
	 * @param msg 当重定向url为空时 响应的消息
	 * @param httpStatus 当重定向url为空时,响应的状态码
	 * @return 返回一个 mono
	 */
	private Mono<Void> doProcessor(ServerHttpRequest request ,ServerHttpResponse response , String redirectAttrName , HttpStatus httpStatus , String msg){
		GlobalMetadata globalMetadata = MetadataContext.getCurrentGlobalMetadata();
		if (globalMetadata == null){
			logger.warn("当前请求 url[{}] headers [{}] 没有申请租户"  ,
					request.getPath().pathWithinApplication().value(),
					request.getHeaders());
			return WebUtils.write("当前请求所属服务没有申请租户" , response , HttpStatus.FORBIDDEN);
		}
		String url = globalMetadata.get(redirectAttrName);
		if (StringUtils.isEmpty(url)){
			return WebUtils.write(msg, response, httpStatus);
		}else {
			try {
				msg = URLEncoder.encode(msg, "utf-8");
				StringBuilder urlSb = new StringBuilder(url);
				// 添加错误信息
				if (url.contains("?")){
					urlSb.append("&");
				}else {
					urlSb.append("?");
				}
				urlSb.append("errorMsg=");
				urlSb.append(msg);
				//添加网络状态码
				urlSb.append("&httpStatus=");
				urlSb.append(httpStatus.value());
				return WebUtils.sendRedirect(urlSb.toString()  , response);
			}catch (UnsupportedEncodingException e) {
				logger.warn(msg , e);
				return WebUtils.write(String.format("重定向失败 [%s]" , e.getMessage()), response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}



}
