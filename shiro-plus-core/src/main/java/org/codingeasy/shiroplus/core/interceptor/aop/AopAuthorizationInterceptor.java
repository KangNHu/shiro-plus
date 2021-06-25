package org.codingeasy.shiroplus.core.interceptor.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.*;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.handler.AuthExceptionHandler;
import org.codingeasy.shiroplus.core.interceptor.AbstractAuthorizationInterceptor;
import org.codingeasy.shiroplus.core.interceptor.AopInvoker;
import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.Logical;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.springframework.aop.support.AopUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

/**
* 基于aop的授权管理  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class AopAuthorizationInterceptor extends AbstractAuthorizationInterceptor implements MethodInterceptor {




	public AopAuthorizationInterceptor(AuthMetadataManager authMetadataManager,
	                                   AuthExceptionHandler authExceptionHandler ,
	                                   EventManager eventManager) {
		super(authMetadataManager, authExceptionHandler , eventManager);
	}


	public AopAuthorizationInterceptor(AuthMetadataManager authMetadataManager ,EventManager eventManager) {
		this(authMetadataManager, new DefaultAuthExceptionHandler() , eventManager);
	}


	/**
	 * 获取权限元信息
	 * <p>1. 从被标记方法上获取</p>
	 * <p>2. 从公被标记类上获取</p>
	 * @param invoker 调用器
	 * @return 返回权限元信息
	 */
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


	protected PermissionMetadata getSuperPermissionMetadata(Invoker invoker){
		return super.getPermissionMetadata(invoker);
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
	protected PermissionMetadata doGetPermissionMetadata(AnnotatedElement annotatedElement){
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
				permissionMetadata.setLogical(org.codingeasy.shiroplus.core.metadata.Logical.form(requiresPermissions.logical()));
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

		@Override
		public void authenticationFailure(Invoker invoker, AuthenticationException e) {
			
		}
	}
}
