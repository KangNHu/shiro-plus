package org.codingeasy.shiro.authorize.interceptor;

import org.aopalliance.aop.Advice;
import org.apache.shiro.authz.annotation.*;
import org.codingeasy.shiro.authorize.annotation.DynamicAuthorization;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
* 基于aop的动态授权拦截器的通知者  
* @author : KangNing Hu
*/
public class AopDynamicAuthorizationAdvisor extends StaticMethodMatcherPointcutAdvisor {


	private List<Class<? extends Annotation>> annotationTypes = new ArrayList<>();


	private AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor;


	public AopDynamicAuthorizationAdvisor(AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor){
		this.aopDynamicAuthorizationInterceptor = aopDynamicAuthorizationInterceptor;
		addAnnotationTypes(
				DynamicAuthorization.class,
				RequiresAuthentication.class,
				RequiresGuest.class,
				RequiresPermissions.class,
				RequiresRoles.class,
				RequiresUser.class
		);
	}


	@Override
	public Advice getAdvice() {
		return super.getAdvice();
	}

	/**
	 * 添加需要拦截的注解类型列表
	 * @param annotationTypes 注解类型列表
	 */
	protected void addAnnotationTypes(Class<? extends Annotation>... annotationTypes){
		for (Class<? extends Annotation> annotationType : annotationTypes){
			this.annotationTypes.add(annotationType);
		}
	}


	@Override
	public boolean matches(Method method, Class<?> aClass) {
		//校验类是否满足条件
		Annotation[] annotations = aClass.getAnnotations();
		for (Annotation annotation : annotations){
			if (annotationTypes.contains(annotation.annotationType())){
				return true;
			}
		}
		//校验方法是否满足条件
		Annotation[] methodAnnotations = method.getAnnotations();
		for (Annotation annotation : methodAnnotations){
			if (annotationTypes.contains(annotation.annotationType())){
				return true;
			}
		}
		return false;
	}
}
