package org.codingeasy.shiro.authorize.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
* 注解相关工具类  
* @author : KangNing Hu
*/
public class AnnotationUtils {

	private static Map<Class , Annotation> map = new HashMap<>();


	/**
	 * 实例化注解
	 * <p>该实现没有对{@link Object}中的方法做处理</p>
	 * <p>仅限用于获取动态创建注解的属性值</p>
	 * @param annotationClass 注解class对象
	 * @param attr 属性
	 * @param <T> 注解类型
	 * @return 返回一个注解对象
	 */
	public static  <T extends Annotation>T instantiateAnnotation(Class<T> annotationClass , Map<String , Object> attr){
		Annotation annotation = map.get(annotationClass);
		AnnotationInstanceInvocationHandler.setAttr(attr);
		if (annotation != null){
			return (T) annotation;
		}
		synchronized (map) {
			if (!map.containsKey(annotationClass)) {
				annotation = (Annotation) Proxy.newProxyInstance(
						annotationClass.getClassLoader(),
						new Class[]{annotationClass},
						new AnnotationInstanceInvocationHandler()
				);
				map.put(annotationClass, annotation);
			}else {
				annotation  = map.get(annotationClass);
			}
		}
		return (T) annotation;
	}



}
