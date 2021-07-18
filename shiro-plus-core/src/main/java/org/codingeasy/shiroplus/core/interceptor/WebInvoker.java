package org.codingeasy.shiroplus.core.interceptor;

import org.apache.shiro.web.util.WebUtils;
import org.codingeasy.shiroplus.core.metadata.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 基于web的调用  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class WebInvoker implements Invoker<HttpServletRequest , HttpServletResponse> {

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
}
