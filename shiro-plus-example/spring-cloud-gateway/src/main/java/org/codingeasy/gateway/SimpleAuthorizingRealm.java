package org.codingeasy.gateway;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

/**
*   
* @author : kangning <a>2035711178@qq.com</a>
*/
@Component
public class SimpleAuthorizingRealm extends AuthorizingRealm {

	private String userId = "123456";


	private String userToken = "abc123456";

	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		if (userId.equals(principalCollection.getPrimaryPrincipal())){
			SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
			simpleAuthorizationInfo.setRoles(new HashSet<String>(Arrays.asList("test" ,"admin")));
			simpleAuthorizationInfo.setStringPermissions(new HashSet<String>(Arrays.asList("add" ,"update" , "delete")));
			return simpleAuthorizationInfo;
		}
		return new SimpleAuthorizationInfo();
	}


	@Override
	public Class getAuthenticationTokenClass() {
		return BearerToken.class;
	}

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		String token = (String) authenticationToken.getPrincipal();
		if (userToken.equals(token)){
			return new SimpleAuthenticationInfo(userId , userToken , "SimpleAuthorizingRealm");
		}
		return null;
	}
}
