package org.codingeasy.shiroplus.loader.admin.server.models;

/**
* 用户的简单信息  
* @author : KangNing Hu
*/
public class UserSimple {

	/**
	 * 用户id
	 */
	private  Long id;

	/**
	 * 用户名称
	 */
	private String username;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
