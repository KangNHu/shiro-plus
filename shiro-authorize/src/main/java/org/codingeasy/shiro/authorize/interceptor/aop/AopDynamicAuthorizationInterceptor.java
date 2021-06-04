package org.codingeasy.shiro.authorize.interceptor.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.annotation.*;
import org.codingeasy.shiro.authorize.handler.AuthExceptionHandler;
import org.codingeasy.shiro.authorize.interceptor.AopInvoker;
import org.codingeasy.shiro.authorize.interceptor.Invoker;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.authorize.metadata.PermiModel;
import org.codingeasy.shiro.authorize.metadata.PermissionMetadata;
import org.springframework.aop.support.AopUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 基于aop的动态权限拦截器
 * <p>
 * 如果使用{@link org.codingeasy.shiro.authorize.annotation.DynamicAuthorization}来标记授权目标，则授权模式以权限元信息为主
 * </p>
 * <p>如果使用{@link org.apache.shiro.authz.annotation.RequiresRoles}来标记授权目标，则授权模式为{@link PermiModel#ROLE}</p>
 * <p>如果使用{@link org.apache.shiro.authz.annotation.RequiresPermissions}来标记授权目标，则授权模式为{@link PermiModel#PERMISSION}</p>
 * <p>如果使用{@link org.apache.shiro.authz.annotation.RequiresAuthentication}来标记授权目标，则授权模式为{@link PermiModel#AUTHENTICATION}</p>
 * <p>如果使用{@link org.apache.shiro.authz.annotation.RequiresGuest}来标记授权目标，则授权模式为{@link PermiModel#PRINCIPAL}</p>
 * <p>如果使用{@link org.apache.shiro.authz.annotation.RequiresUser}来标记授权目标，则授权模式为{@link PermiModel#USER}</p>
 *
 * @author : KangNing Hu
 */
public class AopDynamicAuthorizationInterceptor extends AopAuthorizationInterceptor {



	public AopDynamicAuthorizationInterceptor(AuthMetadataManager authMetadataManager, AuthExceptionHandler authExceptionHandler) {
		super(authMetadataManager, authExceptionHandler);

	}

	public AopDynamicAuthorizationInterceptor(AuthMetadataManager authMetadataManager) {
		super(authMetadataManager);
	}



	@Override
	protected PermissionMetadata getPermissionMetadata(Invoker invoker) {
		PermissionMetadata permissionMetadata = this.authMetadataManager.getPermissionMetadata(invoker.getPermissionMetadataKey());
		AopInvoker aopInvoker = (AopInvoker) invoker;
		MethodInvocation methodInvocation = aopInvoker.getMethodInvocation();
		PermiModel permiModel = getPermiModel(methodInvocation);
		//如果如果方法指定了授权类型则使用指定的授权类型进行授权
		// 否则使用权限元信息数据进行授权处理
		if (permiModel != null){
			permissionMetadata.setPermiModel(permiModel);
		}
		return permissionMetadata;

	}

	/**
	 * 根据调用信息返回注解配置的授权模式
	 * @param invocation aop调用器
	 * @return 返回授权模式
	 */
	private PermiModel getPermiModel(MethodInvocation invocation) {
		//获取方法上的授权模式
		PermiModel methodPermiModel = getPermiModel(invocation.getMethod());
		if (methodPermiModel != null){
			return methodPermiModel;
		}
		//获取类上的授权模式
		Class<?> targetClass = AopUtils.getTargetClass(invocation.getThis());
		return getPermiModel(targetClass);
	}

	/**
	 * 根据annotatedElement获取注解配置的授权模式
	 * @param annotatedElement  注解元素
	 * @return 返回授权模式
	 */
	private PermiModel getPermiModel(AnnotatedElement annotatedElement){
		Annotation[] annotations = annotatedElement.getAnnotations();
		for (Annotation annotation : annotations){
			Class<? extends Annotation> type = annotation.annotationType();
			if (type == RequiresAuthentication.class){
				return PermiModel.AUTHENTICATION;
			}
			if (type == RequiresGuest.class){
				return PermiModel.PRINCIPAL;
			}
			if (type == RequiresPermissions.class){
				return PermiModel.PERMISSION;
			}
			if (type == RequiresRoles.class){
				return PermiModel.ROLE;
			}
			if (type == RequiresUser.class){
				return PermiModel.USER;
			}
		}
		return null;
	}

}
