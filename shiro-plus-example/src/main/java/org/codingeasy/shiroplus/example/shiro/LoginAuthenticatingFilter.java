package org.codingeasy.shiroplus.example.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.codingeasy.shiroplus.springboot.annotaion.ShiroFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
*   
* @author : KangNing Hu
*/
@ShiroFilter("login")
public class LoginAuthenticatingFilter extends AuthenticatingFilter {



	private String username = "admin";

	private String password = "123456";


	protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		//为了方便 这里登陆信息从query中获取 实际生成中 请放form表单提交 或 post application/json提交

		HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
		final String username = httpServletRequest.getParameter("username");
		final String password = httpServletRequest.getParameter("password");
		if (this.username.equals(username) && this.password.equals(password)){
			return new BearerToken("abc123456");
		}
		return null;
	}

	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

		return executeLogin(servletRequest , servletResponse);
	}


	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		issueSuccessRedirect(request, response);
		return true;
	}



	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return false;
	}
}
