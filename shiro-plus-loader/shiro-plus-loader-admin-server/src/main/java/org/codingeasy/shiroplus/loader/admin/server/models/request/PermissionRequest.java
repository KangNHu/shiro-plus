package org.codingeasy.shiroplus.loader.admin.server.models.request;

/**
* 权限分页查询条件  
* @author : KangNing Hu
*/
public class PermissionRequest extends RequestPage{

	/**
	 * 用户名称
	 */
	private String username;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
