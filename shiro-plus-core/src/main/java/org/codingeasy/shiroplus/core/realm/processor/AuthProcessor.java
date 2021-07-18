package org.codingeasy.shiroplus.core.realm.processor;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.realm.RequestToken;

import java.util.Set;

/**
* 权限处理器  
* @author : KangNing Hu
*/
public interface AuthProcessor<R ,S> {

	/**
	 * 获取token
	 * <p>获取当前请求的token</p>
	 * @return 返回token
	 */
	String getToken(R request);

	/**
	 * 校验token 并返回token主体
	 * @param requestToken token
	 * @return 返回token主体 用于后续权限查询
	 */
	Object validate(RequestToken<R> requestToken);

	/**
	 * 获取权限列表
	 * <p>一般为权限标识</p>
	 * @param primaryPrincipal token 主体
	 * @return 返回权限列表 ，可以返回空
	 */
	Set<String> getPermissions(Object primaryPrincipal);

	/**
	 * 获取角色列表
	 * @param primaryPrincipal  token 主体
	 * @return 返回角色列表 ，可以返回空
	 */
	default Set<String> getRoles(Object primaryPrincipal){
		throw new UnsupportedOperationException();
	}


	/**
	 * 生成租户id
	 * @param r 请求对象
	 * @return 返回租户id
	 */
	default String getTenantId(R r){
		return getDefaultTenantId();
	}

	/**
	 * 授权失败处理
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	default void authorizationFailure(R request ,S response, AuthorizationException e){
		throw e;
	}


	/**
	 * 鉴权失败处理
	 * @param request 请求对象
	 * @param response  响应对象
	 */
	default void authenticationFailure(R request ,S response, AuthenticationException e){
		throw e;
	}


	/**
	 * 返回默认的租户id
	 * @return 租户id
	 */
	default String getDefaultTenantId(){
		return AuthMetadataManager.DEFAULT_TENANT_ID;
	}
}
