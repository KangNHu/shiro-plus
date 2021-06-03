package org.codingeasy.shiro.authorize.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
* 注解实例调用处理器  
* @author : KangNing Hu
*/
public class AnnotationInstanceInvocationHandler implements InvocationHandler {

	private static ThreadLocal<Map<String , Object>> ATTR = new ThreadLocal<>();


	/**
	 * 设置注解属性
	 * @param attr 注解实例的属性
	 */
	public static void setAttr(Map<String , Object> attr){
		ATTR.set(attr);
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		Map<String, Object> map = ATTR.get();
		try {
			return map == null ? null : map.get(name);
		}finally {
			ATTR.remove();
		}
	}
}
