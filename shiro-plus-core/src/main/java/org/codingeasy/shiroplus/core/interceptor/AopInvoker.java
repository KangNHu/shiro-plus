package org.codingeasy.shiroplus.core.interceptor;

import org.aopalliance.intercept.MethodInvocation;


/**
* 基于aop的调用器  
* @author : kangning <a>2035711178@qq.com</a>
*/
public  class  AopInvoker implements Invoker<MethodInvocation ,MethodInvocation> {

	private MethodInvocation methodInvocation;


	public AopInvoker(MethodInvocation methodInvocation) {
		this.methodInvocation = methodInvocation;
	}


	public MethodInvocation getMethodInvocation() {
		return methodInvocation;
	}

	@Override
	public Object invoke() {
		try {
			return methodInvocation.proceed();
		}catch (Throwable e){
			throw new IllegalStateException(e);
		}
	}

	@Override
	public MethodInvocation getRequest() {
		return methodInvocation;
	}

	@Override
	public MethodInvocation getResponse() {
		return methodInvocation;
	}

}
