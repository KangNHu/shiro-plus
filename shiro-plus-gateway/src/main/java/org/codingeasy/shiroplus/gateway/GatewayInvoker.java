package org.codingeasy.shiroplus.gateway;

import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
* 网关调用器
* @author : KangNing Hu
*/
public class GatewayInvoker implements Invoker {

	private ServerWebExchange serverWebExchange;

	private GatewayFilterChain gatewayFilterChain;


	public GatewayInvoker(ServerWebExchange serverWebExchange , GatewayFilterChain gatewayFilterChain){
		this.serverWebExchange =serverWebExchange;
		this.gatewayFilterChain = gatewayFilterChain;
	}

	@Override
	public Object invoke() {
		return this.gatewayFilterChain.filter(this.serverWebExchange);
	}


	public ServerWebExchange getServerWebExchange() {
		return serverWebExchange;
	}

	@Override
	public String getPermissionMetadataKey() {
		ServerHttpRequest request = serverWebExchange.getRequest();
		RequestPath path = request.getPath();
		return path.pathWithinApplication().value() + ":" + request.getMethod();
	}
}
