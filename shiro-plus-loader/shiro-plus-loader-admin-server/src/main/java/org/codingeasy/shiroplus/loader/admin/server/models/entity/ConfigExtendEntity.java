package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 配置扩展表
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@TableName("sp_config_extend")
public class ConfigExtendEntity{

    /**
     * 来自*_config 表主键
     */
	private Long configId;
    /**
     * 扩展属性名称
     */
	private String name;
    /**
     * 扩展属性值
     */
	private String value;
    /**
     * 配置表类型 
     */
	private Integer type;


	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}