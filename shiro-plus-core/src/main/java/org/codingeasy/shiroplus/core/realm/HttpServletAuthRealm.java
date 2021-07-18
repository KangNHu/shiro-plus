package org.codingeasy.shiroplus.core.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.codingeasy.shiroplus.core.realm.processor.AuthProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
* 权限realm  
* @author : KangNing Hu
*/
public class HttpServletAuthRealm extends AuthorizingRealm {


	private AuthProcessor<HttpServletRequest , HttpServletResponse> authProcessor;


	public HttpServletAuthRealm(AuthProcessor<HttpServletRequest , HttpServletResponse> authProcessor){
		this.authProcessor = authProcessor;
	}
	/**
	 * 执行授权
	 * @param principalCollection 访问者主题 这里是userId
	 * @return 返回权限信息 ，这里主要为角色编码列表
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		Object primaryPrincipal = principalCollection.getPrimaryPrincipal();
		//获取权限列表
		Set<String> permissions = authProcessor.getPermissions(primaryPrincipal);
		//获取角色列表
		Set<String> roles = authProcessor.getRoles(primaryPrincipal);
		//构建授权信息
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setStringPermissions(permissions);
		authorizationInfo.setRoles(roles);
		return authorizationInfo;
	}


	@Override
	protected void onInit() {
		super.onInit();
		this.setAuthenticationTokenClass(RequestToken.class);
	}

	/**
	 * 执行鉴权
	 * @see RequestToken
	 * @param authenticationToken token
	 * @return 返回访问者主体信息
	 * @throws AuthenticationException 鉴权异常
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		RequestToken<HttpServletRequest> requestToken = (RequestToken) authenticationToken;
		//校验token
		Object principals = authProcessor.validate(requestToken);
		return new SimpleAuthenticationInfo(principals , requestToken.getCredentials() , HttpServletAuthRealm.class.getName());
	}
}
