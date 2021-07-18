package org.codingeasy.shiroplus.loader.admin.server.security.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.codingeasy.shiroplus.core.annotation.ShiroFilter;
import org.codingeasy.shiroplus.loader.admin.server.utils.SystemUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.WebUtils;

import javax.naming.AuthenticationException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* 用户处理admin client请求的过滤器  
* @author : KangNing Hu
*/
@ShiroFilter("adminClient")
public class AdminClientFilter extends AdviceFilter {



	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		if (!(request instanceof HttpServletRequest)){
			return true;
		}
		String token = WebUtils.getToken((HttpServletRequest) request);
		if (!StringUtils.isEmpty(token)){
			throw new AuthenticationException("Invalid certificate");
		}
		if (!token.equals(SystemUtils.getClientToken())){
			throw new AuthenticationException("Invalid certificate");
		}
		return true;
	}


	@Override
	protected void cleanup(ServletRequest request, ServletResponse response, Exception existing) throws ServletException, IOException {
		if (request instanceof HttpServletRequest && existing instanceof AuthenticationException){
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpServletResponse.flushBuffer();
			return;
		}
		super.cleanup(request, response, existing);

	}
}
