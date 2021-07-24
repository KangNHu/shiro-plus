package org.codingeasy.shiroplus.loader.admin.client;


import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.client.model.AuthMetadataEventWrap;
import org.codingeasy.shiroplus.loader.admin.client.model.Response;

import java.util.List;

/**
* admin客户端  
* @author : KangNing Hu
*/
public interface AdminClient {

	/**
	 * 获取所有全局配置
	 * @return
	 */
	@RequestLine("GET /client/admin/config/getGlobalMetadataAll")
	@Headers("Content-Type: application/json")
	Response<List<GlobalMetadata>> getGlobalMetadataAll();


	/**
	 * 获取所有权限配置
	 * @return
	 */
	@RequestLine("GET /client/admin/config/getPermissionMetadataAll")
	@Headers("Content-Type: application/json")
	Response<List<PermissionMetadata>> getPermissionMetadataAll();


	/**
	 * 拉取当前请求待消费的事件
	 * @return
	 */
	@RequestLine("GET /client/admin/config/pullEvents?instanceCode={instanceCode}")
	@Headers("Content-Type: application/json")
	Response<AuthMetadataEventWrap> pullEvents(@Param("instanceCode") String instanceCode);


	/**
	 * 创建实例
	 * @param instanceCode 实例编码
	 * @return
	 */
	@RequestLine("POST /client/admin/createInstance?instanceCode={instanceCode}&applicationName={applicationName}")
	@Headers("Content-Type: application/json")
	Response<Integer> createInstance(@Param("instanceCode") String instanceCode , @Param("applicationName") String applicationName);


	/**
	 * 心跳
	 * @param instanceCode 实例编码
	 * @return
	 */
	@RequestLine("GET /client/admin/ping?instanceCode={instanceCode}")
	@Headers("Content-Type: application/json")
	Response<Integer> ping(@Param("instanceCode") String instanceCode);
}
