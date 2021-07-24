package org.codingeasy.shiroplus.loader.admin.server.controller;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.models.dto.AuthMetadataEventWrap;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.codingeasy.shiroplus.loader.admin.server.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 配置管理  
* @author : KangNing Hu
*/
@RestController
@RequestMapping("/client/admin")
public class ClientConfigController {

	@Autowired
	private ConfigService configService;

	@Autowired
	private SystemService systemService;
	/**
	 * 获取所有全局配置
	 * @return 返回所有的全局配置信息
	 */
	@GetMapping("/config/getGlobalMetadataAll")
	public Response<List<GlobalMetadata>> getGlobalMetadataAll(){
		return Response.ok(configService.getGlobalMetadataAll());
	}

	/**
	 * 获取所有权限配置
	 * @return 返回所有的权限配置信息
	 */
	@GetMapping("/config/getPermissionMetadataAll")
	public Response<List<PermissionMetadata>> getPermissionMetadataAll(){
		return Response.ok(configService.getPermissionMetadataAll());
	}

	/**
	 * 拉取当前实例待消费的事件
	 * @param instanceCode 客户端实例编码
	 * @return 返回所有待消费的事件
	 */
	@GetMapping("/config/pullEvents")
	public Response<AuthMetadataEventWrap> pullEvents(String instanceCode){
		return Response.ok(configService.pullEvents(instanceCode));
	}

	/**
	 * 客户端心跳
	 * @param instanceCode 实例编码
	 * @return 返回 ok 代表服务端存活
	 */
	@GetMapping("/ping")
	public Response<Integer> ping(String instanceCode){
		return Response.ok(systemService.ping(instanceCode));
	}

	/**
	 * 创建实例
	 * @param instanceCode 实例编码
	 * @return
	 */
	@PostMapping("/createInstance")
	public Response<Integer> createInstance(
			@NotNull(message = "实例编码不能为空") String instanceCode ,
			@NotNull(message = "应用名称不能为空") String applicationName ,
			HttpServletRequest request){
		return Response.ok(systemService.createInstance(instanceCode ,applicationName ,request));
	}
}
