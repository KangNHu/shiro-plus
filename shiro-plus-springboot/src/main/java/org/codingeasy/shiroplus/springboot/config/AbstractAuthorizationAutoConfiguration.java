package org.codingeasy.shiroplus.springboot.config;

import org.codingeasy.shiroplus.core.handler.AuthorizationHandler;
import org.codingeasy.shiroplus.core.interceptor.AbstractAuthorizationInterceptor;
import org.codingeasy.shiroplus.core.realm.processor.AuthProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportAware;

import java.util.ArrayList;
import java.util.List;

/**
* 抽象的权限配置  
* @author : kangning <a>2035711178@qq.com</a>
*/
public abstract class AbstractAuthorizationAutoConfiguration<R ,S> {

	protected AuthProcessor<R ,S> authProcessor;


	private List<AuthorizationHandler> authorizationHandlers = new ArrayList<AuthorizationHandler>();


	@Autowired(required = false)
	public void setAuthExceptionHandler(AuthProcessor<R ,S> authProcessor){
		this.authProcessor = authProcessor;
	}


	@Autowired(required = false)
	public void setAuthorizationHandlers(List<AuthorizationHandler> authorizationHandlers){
		this.authorizationHandlers.addAll(authorizationHandlers);
	}


	protected void setCommonComponent(AbstractAuthorizationInterceptor<R ,S> interceptor){
		if (authProcessor != null){
			interceptor.setAuthProcessor(authProcessor);
		}
		for (AuthorizationHandler authorizationHandler : authorizationHandlers){
			interceptor.addAuthorizationHandler(authorizationHandler);
		}
	}




}
