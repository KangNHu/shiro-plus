package org.codingeasy.shiroplus.gateway;

import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

/**
* 网关调用器
* @author : KangNing Hu
*/
public class GatewayInvoker implements Invoker<ServerHttpRequest , ServerHttpResponse> {

	private ServerWebExchange serverWebExchange;

	private GatewayFilterChain gatewayFilterChain;

	private String pathPrefix;

	public GatewayInvoker(ServerWebExchange serverWebExchange ,
	                      GatewayFilterChain gatewayFilterChain , String pathPrefix){
		this.serverWebExchange =serverWebExchange;
		this.gatewayFilterChain = gatewayFilterChain;
		this.pathPrefix = pathPrefix;
	}

	@Override
	public Object invoke() {
		return this.gatewayFilterChain.filter(this.serverWebExchange);
	}

	@Override
	public ServerHttpRequest getRequest() {
		return serverWebExchange.getRequest();
	}

	@Override
	public ServerHttpResponse getResponse() {
		return serverWebExchange.getResponse();
	}


	public ServerWebExchange getServerWebExchange() {
		return serverWebExchange;
	}

}
