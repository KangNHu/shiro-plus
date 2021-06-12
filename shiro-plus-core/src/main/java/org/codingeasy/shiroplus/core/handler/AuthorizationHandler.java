package org.codingeasy.shiroplus.core.handler;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;

/**
* 授权处理器  
* @author : kangning <a>2035711178@qq.com</a>
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

	/**
	 * 获取授权 逻辑类型
	 * @return 返回一个逻辑类型枚举
	 */
	default Logical getLogical(PermissionMetadata permissionMetadata){
		org.codingeasy.shiroplus.core.metadata.Logical logical = permissionMetadata.getLogical();
		return logical == null ?
				Logical.AND :
				org.codingeasy.shiroplus.core.metadata.Logical.AND == logical ? Logical.AND :Logical.OR;
	}
}
