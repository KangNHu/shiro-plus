package org.codingeasy.shiroplus.core.handler;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.aop.RoleAnnotationHandler;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.core.utils.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
*   基于角色的授权处理器
* @author : kangning <a>2035711178@qq.com</a>
*/
public class RoleAuthorizationHandler implements AuthorizationHandler {

	private RoleAnnotationHandler roleAnnotationHandler = new RoleAnnotationHandler();

	@Override
	public void authorize(PermissionMetadata permissionMetadata) {
		Map<String , Object> attr = new HashMap<>();
		attr.put("value" , permissionMetadata.getPermis().toArray(new String[]{}));
		attr.put("logical" , getLogical(permissionMetadata));
		AnnotationUtils.call2(
				an -> roleAnnotationHandler.assertAuthorized(an),
				RequiresRoles.class,
				attr
		);
	}

	@Override
	public boolean support(PermissionMetadata permissionMetadata) {
		return permissionMetadata.getPermiModel() == PermiModel.ROLE;
	}


	public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {

	}
}
