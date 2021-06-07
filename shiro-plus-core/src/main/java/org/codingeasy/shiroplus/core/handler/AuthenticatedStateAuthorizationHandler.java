package org.codingeasy.shiroplus.core.handler;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.aop.AuthenticatedAnnotationHandler;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.core.utils.AnnotationUtils;

import java.util.HashMap;

/**
* 认证状态授权模式处理器  
* @author : KangNing Hu
*/
public class AuthenticatedStateAuthorizationHandler implements AuthorizationHandler {

	private AuthenticatedAnnotationHandler authenticatedAnnotationHandler = new AuthenticatedAnnotationHandler();


	@Override
	public void authorize(PermissionMetadata permissionMetadata) {
		RequiresAuthentication requiresAuthentication = AnnotationUtils.instantiateAnnotation(RequiresAuthentication.class, new HashMap<>());
		authenticatedAnnotationHandler.assertAuthorized(requiresAuthentication);
	}

	@Override
	public boolean support(PermissionMetadata permissionMetadata) {
		return permissionMetadata.getPermiModel() == PermiModel.AUTHENTICATION;
	}
}
