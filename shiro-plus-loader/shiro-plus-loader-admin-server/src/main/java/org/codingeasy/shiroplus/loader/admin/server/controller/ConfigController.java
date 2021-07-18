package org.codingeasy.shiroplus.loader.admin.server.controller;


import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.GlobalConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
* 配置管理  
* @author : KangNing Hu
*/
@RequestMapping("config")
public class ConfigController {

	@Autowired
	private ConfigService configService;

	/**
	 * permission config page
	 * @param request 请求条件
	 * @return 响应符合条件的config page list
	 */
	@PostMapping("/permission/page")
	public Response<Page<PermissionConfigEntity>> permissionPage(@RequestBody PermissionConfigRequest request){
		return Response.ok(configService.permissionPage(request));
	}


	/**
	 * global page config page
	 * @param request 请求条件
	 * @return 响应符合条件的config page list
	 */
	@PostMapping("/global/page")
	public Response<Page<GlobalConfig>> globalPage(@RequestBody GlobalConfigRequest request){
		return Response.ok(configService.globalPage(request));
	}
}
