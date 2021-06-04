package org.codingeasy.shiro.authorize.interceptor.aop;

import org.aopalliance.aop.Advice;

/**
* 基于aop的动态授权拦截器的通知者  
* @author : KangNing Hu
*/
public class AopDynamicAuthorizationAdvisor extends AbstractAuthorizationAdvisor {



	public AopDynamicAuthorizationAdvisor(AopDynamicAuthorizationInterceptor aopDynamicAuthorizationInterceptor){
		super(aopDynamicAuthorizationInterceptor);
	}


	@Override
	public Advice getAdvice() {
		return super.getAdvice();
	}

}
