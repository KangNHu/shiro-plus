package org.codingeasy.shiro.authorize.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
* 注解实例调用处理器  
* @author : KangNing Hu
*/
public class AnnotationInstanceInvocationHandler implements InvocationHandler {

	private static  ThreadLocal<Map<String , Object>> ANNOTATION_ATTR = new ThreadLocal<>();



	/**
	 * 设置注解属性
	 * @param attr 注解实例的属性
	 */
	public static void setAttr(Map<String , Object> attr){
		ANNOTATION_ATTR.set(attr);
	}

	/**
	 * 移除注解属性
	 */
	public static void removeAttr(){
		ANNOTATION_ATTR.remove();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		if ("toString".equals(name)){
			return "tostring was not implemented";
		}
		if ("equals".equals(name)){
			return false;
		}
		if ("hashCode".equals(name)){
			return Objects.hash(proxy);
		}
		Map<String, Object> map = ANNOTATION_ATTR.get();
		return map == null ? null : map.get(name);
	}
}
