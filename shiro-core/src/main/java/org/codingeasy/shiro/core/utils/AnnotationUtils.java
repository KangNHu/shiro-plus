package org.codingeasy.shiro.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
* 注解相关工具类  
* @author : KangNing Hu
*/
public class AnnotationUtils {

	private final static Map<Class , Annotation> map = new HashMap<>();


	/**
	 * 实例化注解
	 * <p>该实现没有对{@link Object}中的方法做处理</p>
	 * <p>仅限用于获取动态创建注解的属性值</p>
	 * @param annotationClass 注解class对象
	 * @param attr 属性
	 * @param <T> 注解类型
	 * @return 返回一个注解对象
	 */
	public static  <T extends Annotation>T instantiateAnnotation(Class annotationClass , Map<String , Object> attr){
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


	/**
	 * 动态注解实例使用的回调
	 * @param function 回调函数
	 * @param annotationClass 注解类型
	 * @param attr 注册属性
	 * @param <T> 注册类型
	 * @param <R> 返回值类型
	 * @return 返回处理的结果
	 */
	public static <T ,R> R call(Function<T ,R > function , Class<T> annotationClass , Map<String , Object> attr ){
		T annotation = instantiateAnnotation(annotationClass, attr);
		try {
			return function.apply(annotation);
		}finally {
			AnnotationInstanceInvocationHandler.removeAttr();
		}
	}

	/**
	 * 动态注解实例使用的回调
	 * @param consumer 回调函数
	 * @param annotationClass 注解类型
	 * @param attr 注册属性
	 * @param <T> 注册类型
	 */
	public static <T> void call2(Consumer<T> consumer , Class<T> annotationClass , Map<String , Object> attr ){
		T annotation = instantiateAnnotation(annotationClass, attr);
		try {
			consumer.accept(annotation);
		}finally {
			AnnotationInstanceInvocationHandler.removeAttr();
		}
	}

}
