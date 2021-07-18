package org.codingeasy.shiroplus.core.realm;

import org.apache.shiro.authc.AuthenticationToken;

/**
* 请求token  
* @author : KangNing Hu
*/
public class RequestToken<T> implements AuthenticationToken {

	/**
	 * 请求对象
	 */
	private  T request;

	/**
	 * token
	 */
	private String token;


	public RequestToken(T request, String token) {
		this.request = request;
		this.token = token;
	}

	public T getRequest() {
		return request;
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
