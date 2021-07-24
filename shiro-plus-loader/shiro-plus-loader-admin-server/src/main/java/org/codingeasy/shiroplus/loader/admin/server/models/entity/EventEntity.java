package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 事件实体  
* @author : KangNing Hu
*/
@TableName("sp_event")
public class EventEntity {

	@TableId(type = IdType.AUTO)
	@TableField("id")
	private Long id;


	/**
	 * 事件内容
	 */
	@TableField("event")
	private String event;


	/**
	 * 实例id
	 */
	@TableField("instance_id")
	private Long instanceId;

	/**
	 * 事件源类型
	 */
	private Integer sourceType;


	public EventEntity() {
	}

	public EventEntity(String event, Long instanceId, Integer sourceType) {
		this.event = event;
		this.instanceId = instanceId;
		this.sourceType = sourceType;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
}
