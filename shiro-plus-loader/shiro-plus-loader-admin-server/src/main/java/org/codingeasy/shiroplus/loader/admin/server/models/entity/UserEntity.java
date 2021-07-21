package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.shiroplus.loader.admin.server.models.Action;
import org.codingeasy.streamrecord.core.annotation.Param;

import javax.validation.constraints.NotNull;

/**
 * 用户表
 *
 * @author hukangning
 * @since 1.0.0 2021-07-01
 */
@TableName("sp_user")
public class UserEntity {

	@NotNull(message = "用户id不能为空" , groups = Action.Update.class)
	@TableId(type = IdType.AUTO)
	@Param(LogsProducer.BUSINESS_ID_KEY)
	private Long id;

	/**
	 * 用户名称
	 */
	@NotNull(message = "用户名称不能为空" , groups =Action.Add.class )
	private String username;
	/**
	 * 密码
	 */
	@NotNull(message = "密码不能为空" , groups = Action.Add.class )
	private String password;
	/**
	 * 创建时间
	 */
	private Long createTm;

	/**
	 * 状态 0 删除 1 正常 2 禁用
	 */
	private Integer status;
	/**
	 * 创建人
	 */
	private Long createBy;
	/**
	 * 更新人
	 */
	private Long updateBy;

	/**
	 * 昵称
	 */
	@NotNull(message = "昵称不能为空")
	private String nickname;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Long createTm) {
		this.createTm = createTm;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
}
