package org.codingeasy.shiroplus.loader.admin.server.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.codingeasy.shiroplus.loader.admin.server.models.LoginInfo;

import javax.servlet.http.HttpServletRequest;

/**
* 登录信息token  
* @author : KangNing Hu
*/
public class LoginInfoToken extends UsernamePasswordToken {

	private HttpServletRequest request;

	private String password;

	private Long userId;

	private String token;

	public LoginInfoToken(HttpServletRequest request , LoginInfo loginInfo){
		super(loginInfo.getUsername() , loginInfo.getPassword());
		this.password = loginInfo.getPassword();
		this.request = request;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public Object getPrincipal() {
		return this.userId;
	}

	/**
	 * 获取密码字符串
	 * @return 返回密码
	 */
	public String getPasswordStr() {
		return this.password;
	}


	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
