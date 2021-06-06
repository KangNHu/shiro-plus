package org.codingeasy.shiro.core.interceptor;


/**
 * 调用器
 * @author hukangning
 */
public interface Invoker {

	/**
	 * 调用
	 * @return 返回调用结果
	 */
	Object invoke();




	/**
	 * 获取权限元信息key
	 * @return 返回一个key
	 */
	String getPermissionMetadataKey();
}
