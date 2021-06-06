package org.codingeasy.shiro.core.handler;

import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiro.core.interceptor.Invoker;

/**
 * 异常
 * @author hukangning
 */
public interface AuthExceptionHandler {

	/**
	 * 授权失败处理
	 * <p>{@link Invoker#invoke()} 方法不能在处理器中进行调用，否则会导致目标方法重复调用两次</p>
	 * @param invoker 调用器
	 */
	void authorizationFailure(Invoker invoker , AuthorizationException e);
}
