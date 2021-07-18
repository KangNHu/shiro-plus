package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 全局配置
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@TableName("sp_global_config")
public class GlobalConfigEntity {
	private static final long serialVersionUID = 1L;

    /**
     * 租户id
     */
	private String tenantId;
    /**
     * 放行白名单
     */
	private String anons;
    /**
     * 是否开启鉴权 0 关闭 1 开启
     */
	private Integer enableAuthentication;
    /**
     * 是否开启授权 0 关闭 1开启
     */
	private Integer enableAuthorization;
    /**
     * 创建时间
     */
	private Long createTm;
    /**
     * 状态 0 删除 1 正常
     */
	private Integer status;
    /**
     * 
     */
	private Date lastUpdateTm;
    /**
     * 创建人
     */
	private Long createBy;
    /**
     * 更新人
     */
	private Long updateBy;


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getAnons() {
		return anons;
	}

	public void setAnons(String anons) {
		this.anons = anons;
	}

	public Integer getEnableAuthentication() {
		return enableAuthentication;
	}

	public void setEnableAuthentication(Integer enableAuthentication) {
		this.enableAuthentication = enableAuthentication;
	}

	public Integer getEnableAuthorization() {
		return enableAuthorization;
	}

	public void setEnableAuthorization(Integer enableAuthorization) {
		this.enableAuthorization = enableAuthorization;
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

	public Date getLastUpdateTm() {
		return lastUpdateTm;
	}

	public void setLastUpdateTm(Date lastUpdateTm) {
		this.lastUpdateTm = lastUpdateTm;
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