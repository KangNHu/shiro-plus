package org.codingeasy.shiroplus.core.handler;

import org.apache.shiro.authz.AuthorizationException;
import org.codingeasy.shiroplus.core.interceptor.Invoker;

/**
 * 异常
 * @author kangning <a>2035711178@qq.com</a>
 */
public interface AuthExceptionHandler {

	/**
	 * 授权失败处理
	 * <p>{@link Invoker#invoke()} 方法不能在处理器中进行调用，否则会导致目标方法重复调用两次</p>
	 * @param invoker 调用器
	 */
	void authorizationFailure(Invoker invoker , AuthorizationException e);
}
