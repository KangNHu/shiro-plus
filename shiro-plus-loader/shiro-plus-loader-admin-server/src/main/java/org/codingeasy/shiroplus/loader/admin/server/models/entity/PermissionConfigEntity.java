package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.shiroplus.loader.admin.server.models.Action;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.Logical;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.PermissionModel;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod;
import org.codingeasy.streamrecord.core.annotation.Param;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限配置表
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@TableName("sp_permission_config")
public class PermissionConfigEntity {


	@TableId(type = IdType.AUTO)
	@Param(LogsProducer.BUSINESS_ID_KEY)
	@NotNull(message = "ID不能为空" , groups = Action.Update.class)
	private Long id;
    /**
     * controller 方法的映射路径
     */
    @NotNull(message = "接口URL不能为空" , groups = Action.Add.class)
	private String path;
    /**
     * 状态 0 删除 1 正常
     */
	private Integer status;
    /**
     * 方法的请求方式 如 get post
     */
    @NotNull(message = "方法的请求方式不能为空" , groups = Action.Add.class)
	private Integer method;
    /**
     * 权限标识列表
     */
    @NotNull(message = "权限标识不能为空" , groups = Action.Add.class)
	private String permis;
    /**
     * 逻辑类型 1 and 2 or
     */
    @NotNull(message = "逻辑符不能为空" , groups = Action.Add.class)
	private Integer logical;
    /**
     * 权限模式 1.角色授权模式 2.权限授权模式 3票据授权模式4.认证状态授权模式5.用户信息存在状态的授权模式
     */
    @NotNull(message = "授权模式不能为空" , groups = Action.Add.class)
	private Integer permiModel;

	/**
	 * 扩展字段
	 */
	@TableField(exist = false)
	private Map<String , Object> extend;
    /**
     * 更新人
     */
	private Long updateBy;
    /**
     * 创建人
     */
	private Long createBy;
    /**
     * 创建时间
     */
	private Long createTm;


	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateTm() {
		return createTm;
	}

	public void setCreateTm(Long createTm) {
		this.createTm = createTm;
	}


	/**
	 * 转元数据对象
	 * @return 返回元数据对象
	 */
	public PermissionMetadata toMetadata(){
		//构建元信息
		PermissionMetadata permissionMetadata = new PermissionMetadata();
		String permis = this.getPermis();
		if (!StringUtils.isEmpty(permis)){
			permissionMetadata.setPermis(Arrays.asList(permis.split(",")));
		}
		permissionMetadata.setPath(this.path);
		permissionMetadata.setPermiModel(PermiModel.form(PermissionModel.form(this.getPermiModel())));
		permissionMetadata.setLogical(org.codingeasy.shiroplus.core.metadata.Logical.form(Logical.form(this.getLogical())));
		permissionMetadata.setMethod(org.codingeasy.shiroplus.core.metadata.RequestMethod.form(RequestMethod.form(this.getMethod())));
		permissionMetadata.setAttr(
				Optional
					.ofNullable(this.extend)
					.orElse(new HashMap<>())
		);
		return permissionMetadata;
	}
}