package org.codingeasy.shiroplus.loader.admin.server.models.request;

import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.streamrecord.core.annotation.Param;

import javax.validation.constraints.NotNull;

/**
* 密码信息
* @author : KangNing Hu
*/
public class PasswordInfoRequest {

	/**
	 * user id
	 */
	@NotNull(message = "用户id不能为空")
	@Param(LogsProducer.BUSINESS_ID_KEY)
	private Long userId;


	/**
	 * 旧密码
	 */
	@NotNull(message = "旧密码不能为空")
	private String oldPassword;


	/**
	 * 新密码
	 */
	@NotNull(message = "新密码不能为空")
	private String newPassword;



	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
