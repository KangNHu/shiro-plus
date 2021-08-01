package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod;
import org.codingeasy.streamrecord.core.annotation.Param;

/**
* api信息  
* @author : KangNing Hu
*/
@TableName("sp_open_api")
public class OpenApiEntity {


	@TableId(type = IdType.AUTO)
	@Param(LogsProducer.BUSINESS_ID_KEY)
	private Long id;
	/**
	 * 接口路径
	 */
	private String path;

	/**
	 * 请求方法
	 */
	private Integer method;

	/**
	 * 描述
	 */
	private String summary;

	public OpenApiEntity() {
	}

	public OpenApiEntity(String path, Integer method, String summary) {
		this.path = path;
		this.method = method;
		this.summary = summary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getMethod() {
		return method;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
