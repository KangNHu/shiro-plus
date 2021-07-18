package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* 用户信息以及角色列表信息  
* @author : KangNing Hu
*/
public class UserRolesEntity extends UserEntity {

	/**
	 * 角色列表
	 */
	private List<RoleEntity> roles;


	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
}
