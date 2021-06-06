package org.codingeasy.shiro.core.handler;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.aop.GuestAnnotationHandler;
import org.codingeasy.shiro.core.metadata.PermiModel;
import org.codingeasy.shiro.core.metadata.PermissionMetadata;
import org.codingeasy.shiro.core.utils.AnnotationUtils;

import java.util.HashMap;

/**
* 票据授权模式处理器  
* @author : KangNing Hu
*/
public class PrincipalAuthorizationHandler implements AuthorizationHandler {

	private GuestAnnotationHandler guestAnnotationHandler = new GuestAnnotationHandler();

	@Override
	public void authorize(PermissionMetadata permissionMetadata) {
		RequiresGuest requiresGuest = AnnotationUtils.instantiateAnnotation(RequiresGuest.class, new HashMap<>());
		guestAnnotationHandler.assertAuthorized(requiresGuest);
	}

	@Override
	public boolean support(PermissionMetadata permissionMetadata) {
		return permissionMetadata.getPermiModel() == PermiModel.PRINCIPAL;
	}
}
