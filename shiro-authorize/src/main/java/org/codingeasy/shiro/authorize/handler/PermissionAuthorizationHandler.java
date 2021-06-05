package org.codingeasy.shiro.authorize.handler;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.codingeasy.shiro.authorize.metadata.PermiModel;
import org.codingeasy.shiro.authorize.metadata.PermissionMetadata;
import org.codingeasy.shiro.authorize.utils.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;

/**
* 基于权限标识的授权处理器  
* @author : KangNing Hu
*/
public class PermissionAuthorizationHandler implements AuthorizationHandler{

	private PermissionAnnotationHandler permissionAnnotationHandler = new PermissionAnnotationHandler();

	@Override
	public void authorize(PermissionMetadata permissionMetadata) {
		Map<String , Object> attr = new HashMap<>();
		attr.put("value" , permissionMetadata.getPermis().toArray(new String[]{}));
		attr.put("logical" , getLogical(permissionMetadata));
		AnnotationUtils.call2(
				an -> permissionAnnotationHandler.assertAuthorized(an),
				RequiresPermissions.class,
				attr
		);
	}

	@Override
	public boolean support(PermissionMetadata permissionMetadata) {
		return permissionMetadata.getPermiModel() == PermiModel.PERMISSION;
	}

}
