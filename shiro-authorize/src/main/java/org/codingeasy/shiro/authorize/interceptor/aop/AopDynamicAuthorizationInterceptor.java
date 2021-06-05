package org.codingeasy.shiro.authorize.interceptor.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.annotation.*;
import org.codingeasy.shiro.authorize.handler.AuthExceptionHandler;
import org.codingeasy.shiro.authorize.annotation.DynamicAuthorization;
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
 * 如果使用{@link DynamicAuthorization}来标记授权目标，则授权模式以权限元信息为主
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


	/**
	 * 获取权限元信息
	 * <p>如果被标记 {@link DynamicAuthorization} 则从{@link AuthMetadataManager}中获取</p>
	 * <p>如果被标记 为 {@link RequiresPermissions} ,{@link RequiresUser} , {@link RequiresGuest}
	 * , {@link RequiresRoles},{@link RequiresAuthentication} 则 从被标记方法上获取授权模式，根据授权模式
	 * 从那个 {@link AuthMetadataManager}中获取 ，如果没有获取到则使用过被标记方法上的权限元信息，否则使用{@link AuthMetadataManager}
	 * 中的元信息
	 * </p>
	 * @param invoker 调用器
	 * @return
	 */
	@Override
	protected PermissionMetadata getPermissionMetadata(Invoker invoker) {
		PermissionMetadata permissionMetadata = super.getPermissionMetadata(invoker);
		if (permissionMetadata == null){
			return super.getSuperPermissionMetadata(invoker);
		}
		String cacheKey = invoker.getPermissionMetadataKey();
		permissionMetadata = this.authMetadataManager.getPermissionMetadata(cacheKey , permissionMetadata.getPermiModel());
		return  permissionMetadata == null ? super.getSuperPermissionMetadata(invoker) : permissionMetadata;

	}



}
