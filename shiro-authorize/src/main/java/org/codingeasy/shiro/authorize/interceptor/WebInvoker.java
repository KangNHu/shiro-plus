package org.codingeasy.shiro.authorize.interceptor;

import org.codingeasy.shiro.authorize.metadata.RequestMethod;
import org.codingeasy.shiro.authorize.utils.HttpUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
	public Object invoke() throws IOException, ServletException {
		filterChain.doFilter(request , response);
		return null;
	}


	@Override
	public String getPermissionMetadataKey() {
		return HttpUtils.getControlPath(this.request) + ":" + RequestMethod.form(request.getMethod());
	}
}
