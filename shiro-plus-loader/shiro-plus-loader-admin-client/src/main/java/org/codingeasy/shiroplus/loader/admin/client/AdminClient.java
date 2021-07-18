package org.codingeasy.shiroplus.loader.admin.client;


import feign.Headers;
import feign.RequestLine;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
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
	@RequestLine("GET /getGlobalConfigs")
	@Headers("Content-Type: application/json")
	Response<List<GlobalMetadata>> getGlobalConfigs();


	/**
	 * 获取所有权限配置
	 * @return
	 */
	@RequestLine("GET /getPermissionConfigs")
	@Headers("Content-Type: application/json")
	Response<List<PermissionMetadata>> getPermissionConfigs();


	/**
	 * 拉取当前请求待消费的事件
	 * @return
	 */
	@RequestLine("GET /pullEvents")
	@Headers("Content-Type: application/json")
	Response<List<AuthMetadataEvent>> pullEvents();
}
