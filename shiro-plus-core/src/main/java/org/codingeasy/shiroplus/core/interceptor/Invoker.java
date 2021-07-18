package org.codingeasy.shiroplus.core.interceptor;


/**
 * 调用器
 * @author kangning <a>2035711178@qq.com</a>
 */
public interface Invoker<R , S> {

	/**
	 * 调用
	 * @return 返回调用结果
	 */
	Object invoke();

	/**
	 * 获取请求对象
	 * @return 返回请求对象
	 */
	R getRequest();


	/**
	 * 获取响应对象
	 * @return 返回响应对象
	 */
	S getResponse();

}
