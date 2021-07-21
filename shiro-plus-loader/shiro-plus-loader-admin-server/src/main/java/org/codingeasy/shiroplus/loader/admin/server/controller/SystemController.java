package org.codingeasy.shiroplus.loader.admin.server.controller;

import org.codingeasy.shiroplus.loader.admin.server.models.Log;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.LogsEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.LogsRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.SystemService;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.SystemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 系统管理  
* @author : KangNing Hu
*/
@RestController
@RequestMapping("/admin/system")
public class SystemController {

	@Autowired
	private SystemService systemService;

	/**
	 * 获取系统配置信息
	 * @return 返回配置信息
	 */
	@GetMapping("")
	public Response<SystemEntity> get(){
		return Response.ok(systemService.get());
	}

	/**
	 * 修改系统配置
	 * @param systemEntity 请求对象
	 * @return 返回响应对象
	 */
	@PostMapping("")
	public Response update(@RequestBody SystemEntity systemEntity){
		return Response.ok(systemService.update(systemEntity));
	}



	@PostMapping("/logsPage")
	public Response<Page<Log>> logsPage(@RequestBody LogsRequest request){
		return Response.ok(systemService.logsPage(request));
	}
}
