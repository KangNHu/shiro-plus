package org.codingeasy.shiroplus.core.interceptor.aop;

import org.aopalliance.aop.Advice;

/**
* 基于shiro 原生的授权拦截器的通知者
* @author : kangning <a>2035711178@qq.com</a>
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
