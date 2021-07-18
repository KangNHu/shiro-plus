package org.codingeasy.shiroplus.core.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.realm.Realm;

/**
* 鉴权的异常策略  
* @author : KangNing Hu
*/
public class AuthenticationExceptionStrategy extends AtLeastOneSuccessfulStrategy {

	@Override
	public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
		if (t == null){
			return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
		}
		if (t instanceof AuthenticationException){
			throw (AuthenticationException)t;
		}else {
			throw new AuthenticationException(t);
		}

	}
}
