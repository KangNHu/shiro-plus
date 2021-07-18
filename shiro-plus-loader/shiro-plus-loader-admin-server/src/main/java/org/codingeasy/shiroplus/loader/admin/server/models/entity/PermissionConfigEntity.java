package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 权限配置表
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@TableName("sp_permission_config")
public class PermissionConfigEntity {

    /**
     * controller 方法的映射路径
     */
	private String path;
    /**
     * 状态 0 删除 1 正常
     */
	private Integer status;
    /**
     * 方法的请求方式 如 get post
     */
	private Integer method;
    /**
     * 权限标识列表
     */
	private String permis;
    /**
     * 逻辑类型 1 and 2 or
     */
	private Integer logical;
    /**
     * 权限模式 1.角色授权模式 2.权限授权模式 3票据授权模式4.认证状态授权模式5.用户信息存在状态的授权模式
     */
	private Integer permiModel;
    /**
     * 更新人
     */
	private Long updateBy;
    /**
     * 创建人
     */
	private Long createBy;
    /**
     * 
     */
	private Date lastUpdateTm;
    /**
     * 创建时间
     */
	private Long createTm;


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMethod() {
		return method;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}

	public String getPermis() {
		return permis;
	}

	public void setPermis(String permis) {
		this.permis = permis;
	}

	public Integer getLogical() {
		return logical;
	}

	public void setLogical(Integer logical) {
		this.logical = logical;
	}

	public Integer getPermiModel() {
		return permiModel;
	}

	public void setPermiModel(Integer permiModel) {
		this.permiModel = permiModel;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getLastUpdateTm() {
		return lastUpdateTm;
	}

	public void setLastUpdateTm(Date lastUpdateTm) {
		this.lastUpdateTm = lastUpdateTm;
	}

	public Long getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Long createTm) {
		this.createTm = createTm;
	}
}