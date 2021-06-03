package org.codingeasy.shiro.authorize.handler;

import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiro.authorize.metadata.PermissionMetadata;

/**
* 授权处理器  
* @author : KangNing Hu
*/
public interface AuthorizationHandler {
	/**
	 * 授权处理
	 * @param permissionMetadata 权限元信息
	 * @throws AuthorizationException 当校验失败时 抛出
	 */
	void authorize(PermissionMetadata permissionMetadata);

	/**
	 * 是否支持
	 * @param permissionMetadata 权限元信息
	 * @return 如果当前处理器执行则返回true  否则falsa
	 */
	boolean support(PermissionMetadata permissionMetadata);
}
