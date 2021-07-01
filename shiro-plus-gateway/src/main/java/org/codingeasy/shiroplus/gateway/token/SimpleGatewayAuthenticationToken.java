package org.codingeasy.shiroplus.gateway.token;

import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;


/**
* 简单的网关鉴权token  
* @author : KangNing Hu
*/
public class SimpleGatewayAuthenticationToken implements GatewayAuthenticationToken {

	private GlobalMetadata globalMetadata;


	private ServerHttpRequest request;


	private String token;


	public SimpleGatewayAuthenticationToken(GlobalMetadata globalMetadata, ServerHttpRequest request, String token) {
		this.globalMetadata = globalMetadata;
		this.request = request;
		this.token = token;
	}

	@Override
	public GlobalMetadata getMetadata() {
		return this.globalMetadata;
	}

	@Override
	public ServerHttpRequest getRequest() {
		return this.request;
	}


	@Override
	public Object getPrincipal() {
		return this.token;
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}
}
