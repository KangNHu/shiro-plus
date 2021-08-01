package org.codingeasy.shiroplus.loader.admin.server.controller;


import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.loader.admin.server.models.Action;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.OpenApiEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.GlobalConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.OpenAPiRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.codingeasy.streamrecord.core.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
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
	public Response<Integer> deleteGlobal(@NotNull(message = "元信息id不能为空") Long id){
		return Response.ok(configService.deleteGlobal(id));
	}

	/**
	 * 获取全局元信息
	 * @param id 元信息id
	 * @return
	 */
	@GetMapping("/global")
	public Response<GlobalConfigEntity> getGlobal(@NotNull(message = "元信息id不能为空") Long id){
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

	/**
	 * permission page config page
	 * @param request 请求条件
	 * @return 响应符合条件的config page list
	 */
	@PostMapping("/permission/page")
	public Response<Page<PermissionConfigEntity>> permissionPage(@RequestBody PermissionConfigRequest request){
		return Response.ok(configService.permissionPage(request));
	}


	/**
	 * 新增权限元信息
	 * @param permissionConfigEntity 待新增的数据
	 * @return
	 */
	@PostMapping("/permission")
	public Response<Integer> addPermission(@RequestBody @Validated(Action.Add.class) PermissionConfigEntity permissionConfigEntity){
		return Response.ok(configService.addPermission(permissionConfigEntity));
	}


	/**
	 * 删除权限元信息
	 * @param id 元信息id
	 * @return
	 */
	@DeleteMapping("/permission")
	public Response<Integer> deletePermission(@NotNull(message = "元信息id不能为空") Long id){
		return Response.ok(configService.deletePermission(id));
	}

	/**
	 * 获取权限元信息
	 * @param id 元信息id
	 * @return
	 */
	@GetMapping("/permission")
	public Response<PermissionConfigEntity> getPermission(@NotNull(message = "元信息id不能为空") Long id){
		return Response.ok(configService.getPermission(id));
	}

	/**
	 * 修改权限元信息
	 * @param permissionConfigEntity 权限元信息
	 * @return
	 */
	@PutMapping("/permission")
	public Response<Integer> updatePermission(@RequestBody @Validated(Action.Update.class) PermissionConfigEntity permissionConfigEntity){
		return Response.ok(configService.updatePermission(permissionConfigEntity));
	}


	/**
	 * 导入api
	 * <p>这只识别swagger 生成的json 文件 具体格式可以参考 swagger api:v2/api-docs</p>
	 * @param multipartFile swagger json的 文件对象
	 * @return
	 */
	@PostMapping("/api/import")
	public Response<Integer> importApi(@RequestParam("file") MultipartFile multipartFile){
		return Response.ok(configService.importApi(multipartFile));
	}

	/**
	 * api接口列表分页
	 * @param request 请对象
	 * @return 返回分页数据
	 */
	@PostMapping("/api/page")
	public Response<Page<OpenApiEntity>> openApiPage(@RequestBody OpenAPiRequest request){
		return Response.ok(configService.apiPage(request));
	}


	/**
	 * 删除api
	 * @param id id
	 * @return
	 */
	@DeleteMapping("/api")
	public Response<Integer> deleteOpenApi(@NotNull(message = "Id不能为空") Long id){
		OpenApiEntity openApiEntity = new OpenApiEntity();
		openApiEntity.setId(id);
		return Response.ok(configService.deleteOpenApi(openApiEntity));
	}

	/**
	 * 用于模糊查询列表
	 * @param path  接口路径
	 * @return 返回列表
	 */
	@GetMapping("/api/like")
	public Response<List<OpenApiEntity>> likeOpenApi(String path){
		return Response.ok(configService.likeOpenApi(path));
	}
}
