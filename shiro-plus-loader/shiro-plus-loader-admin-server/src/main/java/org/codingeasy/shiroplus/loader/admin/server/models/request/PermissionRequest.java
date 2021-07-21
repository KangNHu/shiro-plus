package org.codingeasy.shiroplus.loader.admin.server.models.request;

import java.util.List;

/**
* 权限分页查询条件  
* @author : KangNing Hu
*/
public class PermissionRequest extends RequestPage{

	/**
	 * 用户名称
	 */
	private String username;

	/**
	 * 用户id
	 */
	private Long userId;


	/**
	 * 用户id列表
	 */
	private List<Long> userIds;


	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
