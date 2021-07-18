package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import java.util.List;

/**
* 用户和角色名称列表实体
* @author : KangNing Hu
*/
public class UserRoleNamesEntity extends UserEntity {

	/**
	 * 角色名称列表
	 */
	private List<String> roleNames;


	public List<String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}
}
