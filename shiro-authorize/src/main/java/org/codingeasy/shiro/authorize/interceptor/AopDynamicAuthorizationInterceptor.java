package org.codingeasy.shiro.authorize.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.*;
import org.codingeasy.shiro.authorize.handler.AuthExceptionHandler;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.authorize.metadata.PermiModel;
import org.codingeasy.shiro.authorize.metadata.PermissionMetadata;
import org.springframework.aop.support.AopUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

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
public class AopDynamicAuthorizationInterceptor extends AbstractDynamicAuthorizationInterceptor implements MethodInterceptor {


	private List<Class<? extends Annotation>> shiroAnnotationTypes = new ArrayList<>();


	public AopDynamicAuthorizationInterceptor(AuthMetadataManager authMetadataManager, AuthExceptionHandler authExceptionHandler) {
		super(authMetadataManager, authExceptionHandler);

	}

	public AopDynamicAuthorizationInterceptor(AuthMetadataManager authMetadataManager) {
		this(authMetadataManager, new DefaultAuthExceptionHandler());
		addShiroAnnotationTypes(
				RequiresAuthentication.class,
				RequiresGuest.class,
				RequiresPermissions.class,
				RequiresRoles.class,
				RequiresUser.class
		);
	}


	/**
	 * 添加shiro 框架的注解
	 *
	 * @param shiroAnnotationTypes shiro 框架的注解列表
	 */
	public void addShiroAnnotationTypes(Class<? extends Annotation>... shiroAnnotationTypes) {
		for (Class<? extends Annotation> shiroAnnotationType : shiroAnnotationTypes) {
			this.shiroAnnotationTypes.add(shiroAnnotationType);
		}
	}


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return invoke(new AopInvoker(invocation));
	}


	@Override
	protected PermissionMetadata getPermissionMetadata(Invoker invoker) {
		PermissionMetadata permissionMetadata = super.getPermissionMetadata(invoker);
		AopInvoker aopInvoker = (AopInvoker) invoker;
		MethodInvocation methodInvocation = aopInvoker.getMethodInvocation();
		PermiModel permiModel = getPermiModel(methodInvocation);
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
