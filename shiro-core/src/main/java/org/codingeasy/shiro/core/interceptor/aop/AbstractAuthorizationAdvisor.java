package org.codingeasy.shiro.core.interceptor.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.shiro.authz.annotation.*;
import org.codingeasy.shiro.core.annotation.DynamicAuthorization;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
* 抽象的授权处理通知  
* @author : KangNing Hu
*/
public class AbstractAuthorizationAdvisor extends StaticMethodMatcherPointcutAdvisor {

	private List<Class<? extends Annotation>> annotationTypes = new ArrayList<>();


	private MethodInterceptor methodInterceptor;


	public AbstractAuthorizationAdvisor(MethodInterceptor methodInterceptor){
		this.methodInterceptor = methodInterceptor;
		addAnnotationTypes(
				DynamicAuthorization.class,
				RequiresAuthentication.class,
				RequiresGuest.class,
				RequiresPermissions.class,
				RequiresRoles.class,
				RequiresUser.class
		);
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
	public Advice getAdvice() {
		return methodInterceptor;
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
