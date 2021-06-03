package org.codingeasy.shiro.authorize.mgt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 默认的组合id生成器  
* @author : KangNing Hu
*/
public class DefaultTenantIdGenerator implements TenantIdGenerator{


	@Override
	public String generate(HttpServletRequest request, HttpServletResponse response) {
		return getDefault();
	}
}
