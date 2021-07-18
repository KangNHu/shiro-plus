package org.codingeasy.shiroplus.loader.admin.server.controller;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* 配置管理  
* @author : KangNing Hu
*/
@RestController
@RequestMapping("/client/admin/config")
public class ClientConfigController {

	@Autowired
	private ConfigService configService;

	/**
	 * 获取所有全局配置
	 * @return 返回所有的全局配置信息
	 */
	@GetMapping("/client/getGlobalConfigs")
	public Response<List<GlobalConfigEntity>> getGlobalConfigs(){
		return Response.ok(configService.getGlobalConfigs());
	}

	/**
	 * 获取所有权限配置
	 * @return 返回所有的权限配置信息
	 */
	@GetMapping("/client/getPermissionConfigs")
	public Response<List<PermissionConfigEntity>> getPermissionConfigs(){
		return Response.ok(configService.getPermissionConfigs());
	}

	/**
	 * 拉取当前请求待消费的事件
	 * @return 返回所有待消费的事件
	 */
	@GetMapping("/client/pullEvents")
	public Response<List<AuthMetadataEvent>> pullEvents(){
		return null;
	}
}
