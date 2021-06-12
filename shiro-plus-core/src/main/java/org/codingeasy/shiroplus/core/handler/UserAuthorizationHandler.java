package org.codingeasy.shiroplus.core.handler;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.authz.aop.UserAnnotationHandler;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.core.utils.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
*   基于用户信息是否存在状态的授权处理器
* @author : kangning <a>2035711178@qq.com</a>
*/
public class UserAuthorizationHandler implements AuthorizationHandler {

	private UserAnnotationHandler userAnnotationHandler = new UserAnnotationHandler();

	@Override
	public void authorize(PermissionMetadata permissionMetadata) {
		RequiresUser requiresUser = AnnotationUtils.instantiateAnnotation(RequiresUser.class, new HashMap<>());
		userAnnotationHandler.assertAuthorized(requiresUser);
	}

	@Override
	public boolean support(PermissionMetadata permissionMetadata) {
		return permissionMetadata.getPermiModel() == PermiModel.ROLE;
	}


	public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {

	}
}
