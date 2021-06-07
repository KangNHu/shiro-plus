package org.codingeasy.shiroplus.core.handler;

import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;

import java.util.ArrayList;
import java.util.List;

/**
* 所有授权处理器的代理处理器
* @author : KangNing Hu
*/
public class ProxyAuthorizationHandler implements AuthorizationHandler{

	/**
	 * 授权处理器列表
	 */
	private List<AuthorizationHandler> authorizationHandlers = new ArrayList<>();



	/**
	 * 新增授权处理器
	 * @param authorizationHandler 授权处理器
	 */
	public void addAuthorizationHandler(AuthorizationHandler authorizationHandler){
		this.authorizationHandlers.add(authorizationHandler);
	}



	@Override
	public void authorize(PermissionMetadata permissionMetadata) {
		for (AuthorizationHandler authorizationHandler : authorizationHandlers){
			if (authorizationHandler.support(permissionMetadata)){
				authorizationHandler.authorize(permissionMetadata);
			}
		}
	}

	@Override
	public boolean support(PermissionMetadata permissionMetadata) {
		return true;
	}

}
