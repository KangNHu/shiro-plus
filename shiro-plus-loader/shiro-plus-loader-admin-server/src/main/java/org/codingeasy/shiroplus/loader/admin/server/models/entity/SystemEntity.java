package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotNull;

/**
* 系统  
* @author : KangNing Hu
*/
@TableName("sp_system")
public class SystemEntity {

	public static final String VERSION = "1.0.0";


	/**
	 * 版本
	 */
	@TableId
	private String version;
	/**
	 * 事件有效时间
	 */
	@NotNull(message = "事件有效时间不能改为空")
	private Long eventTime;


	/**
	 * 登录token的签名密钥
	 */
	@NotNull(message = "登录token的签名密钥不能为空")
	private String keyPair;


	/**
	 * 客户端token
	 */
	@NotNull(message = "客户端token不能为空")
	private String clientToken;

	/**
	 * 登录有效时间
	 */
	@NotNull(message = "登录有效时间不能为空")
	private Integer loginValidTime;

	/**
	 * 初始化密码
	 */
	@NotNull(message = "初始化密码不能为空")
	private String initPassword;


	public String getInitPassword() {
		return initPassword;
	}

	public void setInitPassword(String initPassword) {
		this.initPassword = initPassword;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getEventTime() {
		return eventTime;
	}

	public void setEventTime(Long eventTime) {
		this.eventTime = eventTime;
	}

	public String getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(String keyPair) {
		this.keyPair = keyPair;
	}

	public String getClientToken() {
		return clientToken;
	}

	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}

	public static String getVERSION() {
		return VERSION;
	}

	public Integer getLoginValidTime() {
		return loginValidTime;
	}

	public void setLoginValidTime(Integer loginValidTime) {
		this.loginValidTime = loginValidTime;
	}
}
