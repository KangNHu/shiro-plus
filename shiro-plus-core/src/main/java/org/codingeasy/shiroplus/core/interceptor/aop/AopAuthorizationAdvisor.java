package org.codingeasy.shiroplus.core.interceptor.aop;

import org.aopalliance.aop.Advice;

/**
* 基于shiro 原生的授权拦截器的通知者
* @author : KangNing Hu
*/
public class AopAuthorizationAdvisor extends AbstractAuthorizationAdvisor {



	public AopAuthorizationAdvisor(AopAuthorizationInterceptor aopAuthorizationInterceptor){
		super(aopAuthorizationInterceptor);
	}


	@Override
	public Advice getAdvice() {
		return super.getAdvice();
	}

}
