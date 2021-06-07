package org.codingeasy.shiroplus.core.interceptor;

import org.codingeasy.shiroplus.core.metadata.RequestMethod;
import org.codingeasy.shiroplus.core.utils.HttpUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 基于web的调用  
* @author : KangNing Hu
*/
public class WebInvoker implements Invoker {

	private HttpServletRequest request;


	private HttpServletResponse response;


	private FilterChain filterChain;


	public WebInvoker(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		this.request = request;
		this.response = response;
		this.filterChain = filterChain;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public Object invoke() {
		try {
			if (response.isCommitted()){
				return null;
			}
			filterChain.doFilter(request, response);
		}catch (Throwable e){
			throw new IllegalStateException(e);
		}
		return null;
	}


	@Override
	public String getPermissionMetadataKey() {
		return HttpUtils.getControlPath(this.request) + ":" + RequestMethod.form(request.getMethod());
	}
}
