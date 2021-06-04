package org.codingeasy.shiro.springboot.config;

import org.codingeasy.shiro.authorize.handler.AuthExceptionHandler;
import org.codingeasy.shiro.authorize.handler.AuthorizationHandler;
import org.codingeasy.shiro.authorize.interceptor.AbstractAuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
* 抽象的权限配置  
* @author : KangNing Hu
*/
public abstract class AbstractAuthorizationAutoConfiguration {

	protected AuthExceptionHandler authExceptionHandler;


	private List<AuthorizationHandler> authorizationHandlers = new ArrayList<AuthorizationHandler>();


	@Autowired(required = false)
	public void setAuthExceptionHandler(AuthExceptionHandler authExceptionHandler){
		this.authExceptionHandler = authExceptionHandler;
	}


	@Autowired(required = false)
	public void setAuthorizationHandlers(List<AuthorizationHandler> authorizationHandlers){
		this.authorizationHandlers.addAll(authorizationHandlers);
	}


	protected void addAuthorizationHandlers(AbstractAuthorizationInterceptor interceptor){
		for (AuthorizationHandler authorizationHandler : authorizationHandlers){
			interceptor.addAuthorizationHandler(authorizationHandler);
		}
	}

}
