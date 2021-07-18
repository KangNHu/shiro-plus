package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@TableName("sp_role")
public class RoleEntity implements Serializable {

	@TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 角色名称
     */
	private String name;
    /**
     * 角色编码
     */
	private String code;

	/**
	 * 描述
	 */
	private String description;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}