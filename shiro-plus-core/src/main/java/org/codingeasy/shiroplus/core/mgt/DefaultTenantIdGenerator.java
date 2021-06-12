package org.codingeasy.shiroplus.core.mgt;

import org.codingeasy.shiroplus.core.interceptor.Invoker;

/**
* 默认的租户id生成器
* @author : kangning <a>2035711178@qq.com</a>
*/
public class DefaultTenantIdGenerator implements TenantIdGenerator{


	@Override
	public String generate(Invoker invoker) {
		return getDefault();
	}
}
