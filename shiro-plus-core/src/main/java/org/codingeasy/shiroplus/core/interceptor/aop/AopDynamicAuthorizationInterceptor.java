package org.codingeasy.shiroplus.core.interceptor.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.annotation.*;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.annotation.DynamicAuthorization;
import org.codingeasy.shiroplus.core.interceptor.AopInvoker;
import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.core.utils.PathUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

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
 * @author : kangning <a>2035711178@qq.com</a>
 */
public class AopDynamicAuthorizationInterceptor extends AopAuthorizationInterceptor {



	public AopDynamicAuthorizationInterceptor(AuthMetadataManager authMetadataManager, EventManager eventManager) {
		super(authMetadataManager , eventManager);

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
	protected PermissionMetadata getPermissionMetadata(Invoker<MethodInvocation, MethodInvocation> invoker) {
		PermissionMetadata permissionMetadata = super.getPermissionMetadata(invoker);
		if (permissionMetadata == null){
			return super.getSuperPermissionMetadata(invoker);
		}

		String cacheKey = super.getPermissionMetadataKey(invoker.getRequest());
		permissionMetadata = this.authMetadataManager.getPermissionMetadata(cacheKey , permissionMetadata.getPermiModel());
		return  permissionMetadata == null ? super.getSuperPermissionMetadata(invoker) : permissionMetadata;

	}


	@Override
	protected boolean isEnableAuthorization(Invoker<MethodInvocation, MethodInvocation> invoker) {
		AopInvoker aopInvoker = (AopInvoker) invoker;
		Method method = aopInvoker.getMethodInvocation().getMethod();
		//获取全局配置
		GlobalMetadata globalMetadata = super.getGlobalMetadata(invoker.getRequest());
		if (globalMetadata == null){
			return true;
		}
		//处理anon 列表（白名单列表）
		if (PathUtils.matches(globalMetadata.getAnons() , method.getName())){
			return false;
		}
		//处理总开关
		return globalMetadata.getEnableAuthorization() == null || globalMetadata.getEnableAuthorization();
	}


	public static void main(String[] args) {
		System.out.println(PathUtils.matches(Arrays.asList("com.test.*.getUser") , "com.test.UserService.getUser"));
	}
}
