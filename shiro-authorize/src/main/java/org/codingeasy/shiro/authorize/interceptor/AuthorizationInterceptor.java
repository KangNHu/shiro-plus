package org.codingeasy.shiro.authorize.interceptor;

/**
* 权限校验拦截器
* @author : KangNing Hu
*/
public  interface AuthorizationInterceptor {


	/**
	 * 拦截调用
	 * @param invoker 调用器
	 * @return 返回返回值
	 * @throws IllegalArgumentException 调用异常
	 */
	Object invoke(Invoker invoker) ;
}
