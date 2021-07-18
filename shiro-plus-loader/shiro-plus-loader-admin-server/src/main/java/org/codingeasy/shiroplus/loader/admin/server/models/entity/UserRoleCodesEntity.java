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
public class UserRoleCodesEntity extends UserEntity {

	/**
	 * 角色编码列表
	 */
	private List<String> roles;


	public List<String> getRoles() {
		return roles;
	}


	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
