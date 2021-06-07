package org.codingeasy.shiroplus.core.interceptor;

import org.aopalliance.intercept.MethodInvocation;


/**
* 基于aop的调用器  
* @author : KangNing Hu
*/
public  class  AopInvoker implements Invoker {

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
	public String getPermissionMetadataKey() {
		return methodInvocation.getMethod().toGenericString();
	}
}
