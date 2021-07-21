package org.codingeasy.shiroplus.loader.admin.server.service;

import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.RoleEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRolesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionRequest;

import java.util.List;

public interface PermissionService {


	/**
	 * 用户角色分页列表
	 * @param request 查询条件
	 * @return 返回用户分页列表信息
	 */
	Page<UserRolesEntity> page(PermissionRequest request);

	/**
	 * 详情
	 * @param userId 用户id
	 * @return  返回响应结果  用户名称 和 角色列表
	 */
	UserRolesEntity get(Long userId);

	/**
	 * 修改用户角色
	 * @param userRolesEntity 用户角色列表信息
	 * @return 响应修改结果
	 */
	int update(UserRolesEntity userRolesEntity);


	/**
	 * 获取角色列表
	 * @return
	 */
	List<RoleEntity> getRoles();

}
