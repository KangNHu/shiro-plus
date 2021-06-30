package org.codingeasy.shiroplus.gateway;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiroplus.core.handler.AuthExceptionHandler;
import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.mgt.TenantIdGenerator;
import org.codingeasy.shiroplus.gateway.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
* 权限异常重定义处理  
* @author : KangNing Hu
*/
public class GatewayAuthExceptionHandler implements AuthExceptionHandler , Ordered {

	private static final Logger log = LoggerFactory.getLogger(GatewayAuthExceptionHandler.class);

	private TenantIdGenerator tenantIdGenerator;

	private AuthMetadataManager authMetadataManager;

	private ThreadLocal<Mono<Void>> redirectMono = new ThreadLocal<>();

	private static final String UNAUTHORIZED_URL = "unauthorized-url";
	private static final String UNAUTHENTICATED_URL = "unauthenticated-url";

	public GatewayAuthExceptionHandler(TenantIdGenerator tenantIdGenerator ,
	                                   AuthMetadataManager authMetadataManager){
		this.authMetadataManager = authMetadataManager;
		this.tenantIdGenerator = tenantIdGenerator;
	}

	@Override
	public void authorizationFailure(Invoker invoker, AuthorizationException e) {
		redirectMono.set(doProcessor(invoker  , UNAUTHORIZED_URL ,
				HttpStatus.UNAUTHORIZED , e.getMessage()));
	}

	@Override
	public void authenticationFailure(Invoker invoker, AuthenticationException e) {
		redirectMono.set(doProcessor(invoker  , UNAUTHENTICATED_URL ,
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
	 * 获取全局元信息
	 * @param invoker 调用器
	 * @return 返回当前请求所对应的元信息
	 */
	private GlobalMetadata getGlobalMetadata(Invoker invoker){
		String tenantId = tenantIdGenerator.generate(invoker);
		return authMetadataManager.getGlobalMetadata(tenantId);
	}

	/**
	 * 执行重定向
	 * @param invoker 调用器
	 * @param redirectAttrName 在{@link GlobalMetadata}中重定向属性名称
	 * @param msg 当重定向url为空时 响应的消息
	 * @param httpStatus 当重定向url为空时,响应的状态码
	 * @return 返回一个 mono
	 */
	private Mono<Void> doProcessor(Invoker invoker , String redirectAttrName , HttpStatus httpStatus , String msg){
		GlobalMetadata globalMetadata = getGlobalMetadata(invoker);
		ServerHttpRequest request = getRequest(invoker);
		ServerHttpResponse response = getResponse(invoker);
		if (globalMetadata == null){
			return WebUtils.write(String.format(
					"当前请求 url[%s] headers [%s] 没有申请租户" ,
					request.getPath().pathWithinApplication().value(),
					request.getHeaders()
			) ,response , HttpStatus.FORBIDDEN );

		}
		String url = globalMetadata.get(redirectAttrName);
		if (StringUtils.isEmpty(url)){
			return WebUtils.write(msg, response, httpStatus);
		}else {
			try {
				msg = URLEncoder.encode(msg , "utf-8");
			}catch (UnsupportedEncodingException e) {
				try {
					msg = URLEncoder.encode("redirect param encoder error" ,"utf-8");
				} catch (UnsupportedEncodingException ex) {
					///
				}
				log.warn(msg , e);
			}
			// 添加错误信息
			if (url.contains("?")){
				url += "&errorMsg=" + msg;
			}else {
				url += "?errorMsg=" + msg;
			}
			//添加网络状态码
			url += "&httpStatus=" + httpStatus.value();
			return WebUtils.sendRedirect(url , getResponse(invoker));
		}
	}

	/**
	 * 获取响应对象
 	 * @param invoker 调用器
	 * @return 返回网关的响应对象
	 */
	private ServerHttpResponse getResponse(Invoker invoker){
		GatewayInvoker gatewayInvoker = (GatewayInvoker) invoker;
		return gatewayInvoker.getServerWebExchange().getResponse();
	}

	/**
	 * 获取请求对象
	 * @param invoker 调用器
	 * @return 返回网关的请求对象
	 */
	private ServerHttpRequest getRequest(Invoker invoker){
		GatewayInvoker gatewayInvoker = (GatewayInvoker) invoker;
		return gatewayInvoker.getServerWebExchange().getRequest();
	}


	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
