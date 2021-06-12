package org.codingeasy.shiroplus.core.mgt;

import org.codingeasy.shiroplus.core.interceptor.Invoker;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;

/**
* 租户id生成器  
* @author : kangning <a>2035711178@qq.com</a>
*/
public interface TenantIdGenerator {

	/**
	 * 生成租户id
	 * @param invoker 调用对象
	 * @return 返回租户id
	 */
	String generate(Invoker invoker);


	/**
	 * 返回默认的租户id
	 * @return 租户id
	 */
	default String getDefault(){
		return AuthMetadataManager.DEFAULT_TENANT_ID;
	}
}
