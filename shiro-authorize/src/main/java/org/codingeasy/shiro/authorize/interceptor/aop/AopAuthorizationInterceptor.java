package org.codingeasy.shiro.authorize.interceptor.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.*;
import org.codingeasy.shiro.authorize.handler.AuthExceptionHandler;
import org.codingeasy.shiro.authorize.interceptor.AbstractAuthorizationInterceptor;
import org.codingeasy.shiro.authorize.interceptor.AopInvoker;
import org.codingeasy.shiro.authorize.interceptor.Invoker;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.authorize.metadata.Logical;
import org.codingeasy.shiro.authorize.metadata.PermiModel;
import org.codingeasy.shiro.authorize.metadata.PermissionMetadata;
import org.springframework.aop.support.AopUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

/**
* 基于aop的授权管理  
* @author : KangNing Hu
*/
public class AopAuthorizationInterceptor extends AbstractAuthorizationInterceptor implements MethodInterceptor {




	public AopAuthorizationInterceptor(AuthMetadataManager authMetadataManager, AuthExceptionHandler authExceptionHandler) {
		super(authMetadataManager, authExceptionHandler);
	}


	public AopAuthorizationInterceptor(AuthMetadataManager authMetadataManager) {
		this(authMetadataManager, new DefaultAuthExceptionHandler());
	}


	@Override
	protected PermissionMetadata getPermissionMetadata(Invoker invoker) {
		AopInvoker aopInvoker = (AopInvoker) invoker;
		MethodInvocation methodInvocation = aopInvoker.getMethodInvocation();
		PermissionMetadata permissionMetadata = doGetPermissionMetadata(methodInvocation.getMethod());
		if (permissionMetadata != null){
			return permissionMetadata;
		}
		Class<?> targetClass = AopUtils.getTargetClass(methodInvocation.getThis());
		return doGetPermissionMetadata(targetClass);
	}


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return invoke(new AopInvoker(invocation));
	}



	/**
	 * 根据annotatedElement获取注解配置的授权模式
	 * @param annotatedElement  注解元素
	 * @return 返回授权模式
	 */
	private PermissionMetadata doGetPermissionMetadata(AnnotatedElement annotatedElement){
		Annotation[] annotations = annotatedElement.getAnnotations();
		PermissionMetadata permissionMetadata = new PermissionMetadata();
		for (Annotation annotation : annotations){
			Class<? extends Annotation> type = annotation.annotationType();
			if (type == RequiresAuthentication.class){
				permissionMetadata.setPermiModel(PermiModel.AUTHENTICATION);
				return permissionMetadata;
			}
			if (type == RequiresGuest.class){
				permissionMetadata.setPermiModel(PermiModel.PRINCIPAL);
				return permissionMetadata;
			}
			if (type == RequiresPermissions.class){
				permissionMetadata.setPermiModel(PermiModel.PERMISSION);
				RequiresPermissions requiresPermissions = (RequiresPermissions) annotation;
				permissionMetadata.setPermis(Arrays.asList(requiresPermissions.value()));
				permissionMetadata.setLogical(Logical.form(requiresPermissions.logical()));
				return permissionMetadata;
			}
			if (type == RequiresRoles.class){
				permissionMetadata.setPermiModel(PermiModel.ROLE);
				RequiresRoles requiresRoles = (RequiresRoles) annotation;
				permissionMetadata.setPermis(Arrays.asList(requiresRoles.value()));
				permissionMetadata.setLogical(Logical.form(requiresRoles.logical()));
				return permissionMetadata;
			}
			if (type == RequiresUser.class){
				permissionMetadata.setPermiModel(PermiModel.PRINCIPAL);
				return permissionMetadata;
			}
		}
		return null;
	}

	/**
	 * 默认异常处理器
	 */
	static class DefaultAuthExceptionHandler implements AuthExceptionHandler {

		@Override
		public void authorizationFailure(Invoker invoker , AuthorizationException e) {
			throw e;
		}
	}
}
