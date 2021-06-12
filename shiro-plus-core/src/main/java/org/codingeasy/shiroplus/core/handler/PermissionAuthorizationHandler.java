package org.codingeasy.shiroplus.core.handler;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.core.utils.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;

/**
* 基于权限标识的授权处理器  
* @author : kangning <a>2035711178@qq.com</a>
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
