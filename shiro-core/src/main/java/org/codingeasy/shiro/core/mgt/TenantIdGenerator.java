package org.codingeasy.shiro.core.mgt;

import org.codingeasy.shiro.core.interceptor.Invoker;
import org.codingeasy.shiro.core.metadata.AuthMetadataManager;

/**
* 租户id生成器  
* @author : KangNing Hu
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
