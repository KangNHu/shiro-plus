package org.codingeasy.shiroplus.loader.admin.server.controller;

import org.codingeasy.shiroplus.loader.admin.server.service.PermissionService;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleNamesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRolesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* 权限管理  
* @author : KangNing Hu
*/
@RestController
@RequestMapping("/admin/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	/**
	 * 用户角色分页列表
	 * @param request 查询条件
	 * @return 返回响应结果 用户分页列表信息
	 */
	@PostMapping("/page")
	public Response<Page<UserRoleNamesEntity>> page(@RequestBody PermissionRequest request){
		return Response.ok(permissionService.page(request));
	}

	/**
	 * 详情
	 * @param userId 用户id
	 * @return  返回响应结果  用户名称 和 角色列表
	 */
	@GetMapping("")
	public Response<UserRolesEntity> get(Long userId){
		return Response.ok(permissionService.get(userId));
	}


	/**
	 * 修改用户角色
	 * @param userRolesEntity 用户角色列表信息
	 * @return 响应修改结果
	 */
	@PutMapping("")
	public Response update(UserRolesEntity userRolesEntity){
		return Response.ok(permissionService.update(userRolesEntity));
	}
}
