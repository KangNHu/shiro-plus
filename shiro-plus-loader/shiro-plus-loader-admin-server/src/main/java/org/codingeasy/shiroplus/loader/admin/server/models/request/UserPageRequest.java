package org.codingeasy.shiroplus.loader.admin.server.models.request;

/**
* 用户分页条件  
* @author : KangNing Hu
*/
public class UserPageRequest extends RequestPage {

	/**
	 * 登录名称
	 */
	private String username;

	/**
	 * 用户昵称
	 */
	private String nickname;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
