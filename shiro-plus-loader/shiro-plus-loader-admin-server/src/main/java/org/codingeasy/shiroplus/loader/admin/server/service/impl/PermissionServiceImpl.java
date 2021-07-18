package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleNamesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRolesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.PermissionService;
import org.codingeasy.streamrecord.core.annotation.RecordService;

/**
* 权限管理  
* @author : KangNing Hu
*/
@RecordService
public class PermissionServiceImpl implements PermissionService {
	@Override
	public Page<UserRoleNamesEntity> page(PermissionRequest request) {
		return null;
	}

	@Override
	public UserRolesEntity get(Long userId) {
		return null;
	}

	@Override
	public int update(UserRolesEntity userRolesEntity) {
		return 0;
	}
}
