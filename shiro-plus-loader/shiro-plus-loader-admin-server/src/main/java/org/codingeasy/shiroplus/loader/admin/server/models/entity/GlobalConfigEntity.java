package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.shiroplus.loader.admin.server.models.Action;
import org.codingeasy.streamrecord.core.annotation.Param;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

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
	 * id
	 */
	@TableId(type = IdType.AUTO)
	@Param(LogsProducer.BUSINESS_ID_KEY)
	@NotNull(message = "id不能为空" , groups = Action.Update.class)
	private Long id;
    /**
     * 租户id
     */
    @NotNull(message = "租户id不能为空",groups = Action.Add.class)
	private String tenantId;
    /**
     * 放行白名单
     */
	private String anons;
    /**
     * 是否开启鉴权 0 关闭 1 开启
     */
    @NotNull(message = "鉴权开启标识不能为空" ,groups =  Action.Add.class)
	private Integer enableAuthentication;
    /**
     * 是否开启授权 0 关闭 1开启
     */
    @NotNull(message = "授权开启标识不能为空" ,groups =  Action.Add.class)
	private Integer enableAuthorization;

	/**
	 * 扩展字段
	 */
	@TableField(exist = false)
	private Map<String , Object> extend;
    /**
     * 创建时间
     */
	private Long createTm;
    /**
     * 状态 0 删除 1 正常
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

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