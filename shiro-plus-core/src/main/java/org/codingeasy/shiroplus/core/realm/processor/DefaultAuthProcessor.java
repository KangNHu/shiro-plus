package org.codingeasy.shiroplus.core.realm.processor;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiroplus.core.realm.RequestToken;

import java.util.Set;

/**
* 默认的权限处理器  
* @author : KangNing Hu
*/
public class DefaultAuthProcessor<R ,S> implements AuthProcessor<R ,S>{


	@Override
	public String getToken(R request) {
		throw  new UnsupportedOperationException();
	}

	@Override
	public Object validate(RequestToken<R> requestToken) {
		throw  new UnsupportedOperationException();
	}

	@Override
	public Set<String> getPermissions(Object primaryPrincipal) {
		throw  new UnsupportedOperationException();
	}

	@Override
	public void authorizationFailure(R request, S response, AuthorizationException e) {
		throw e;
	}

	@Override
	public void authenticationFailure(R request, S response, AuthenticationException e) {
		throw e;
	}
}
