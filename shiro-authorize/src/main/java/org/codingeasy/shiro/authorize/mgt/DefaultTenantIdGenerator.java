package org.codingeasy.shiro.authorize.mgt;

import org.codingeasy.shiro.authorize.interceptor.Invoker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 默认的组合id生成器  
* @author : KangNing Hu
*/
public class DefaultTenantIdGenerator implements TenantIdGenerator{


	@Override
	public String generate(Invoker invoker) {
		return getDefault();
	}
}
