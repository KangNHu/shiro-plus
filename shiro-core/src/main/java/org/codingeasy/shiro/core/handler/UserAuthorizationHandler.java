package org.codingeasy.shiro.core.handler;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.authz.aop.UserAnnotationHandler;
import org.codingeasy.shiro.core.metadata.PermiModel;
import org.codingeasy.shiro.core.metadata.PermissionMetadata;
import org.codingeasy.shiro.core.utils.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
*   基于用户信息是否存在状态的授权处理器
* @author : KangNing Hu
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