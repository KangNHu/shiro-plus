package org.codingeasy.shiroplus.loader.admin.server.controller;


import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.loader.admin.server.models.Action;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.GlobalConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 配置管理  
* @author : KangNing Hu
*/
@RestController
@RequestMapping("/admin/config")
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
	public Response<Page<GlobalConfigEntity>> globalPage(@RequestBody GlobalConfigRequest request){
		return Response.ok(configService.globalPage(request));
	}


	/**
	 * 新增全局元信息
	 * @param globalConfigEntity 待新增的数据
	 * @return
	 */
	@PostMapping("/global")
	public Response<Integer> addGlobal(@RequestBody @Validated(Action.Add.class) GlobalConfigEntity globalConfigEntity){
		return Response.ok(configService.addGlobal(globalConfigEntity));
	}


	/**
	 * 删除全局元信息
	 * @param id 元信息id
	 * @return
	 */
	@DeleteMapping("/global")
	public Response<Integer> deleteGlobal(Long id){
		return Response.ok(configService.deleteGlobal(id));
	}

	/**
	 * 获取全局元信息
	 * @param id 元信息id
	 * @return
	 */
	@GetMapping("/global")
	public Response<GlobalConfigEntity> getGlobal(Long id){
		return Response.ok(configService.getGlobal(id));
	}

	/**
	 * 修改全局元信息
	 * @param globalConfigEntity 全局元信息
	 * @return
	 */
	@PutMapping("/global")
	public Response<Integer> updateGlobal(@RequestBody @Validated(Action.Update.class) GlobalConfigEntity globalConfigEntity){
		return Response.ok(configService.updateGlobal(globalConfigEntity));
	}
}
