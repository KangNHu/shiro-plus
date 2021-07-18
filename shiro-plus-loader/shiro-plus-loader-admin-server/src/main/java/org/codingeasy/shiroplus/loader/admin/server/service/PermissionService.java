package org.codingeasy.shiroplus.loader.admin.server.service;

import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleNamesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRolesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionRequest;

public interface PermissionService {


	/**
	 * 用户角色分页列表
	 * @param request 查询条件
	 * @return 返回用户分页列表信息
	 */
	Page<UserRoleNamesEntity> page(PermissionRequest request);

	UserRolesEntity get(Long userId);

	int update(UserRolesEntity userRolesEntity);
}
