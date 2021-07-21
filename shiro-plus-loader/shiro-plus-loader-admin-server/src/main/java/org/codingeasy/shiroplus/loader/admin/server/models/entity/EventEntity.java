package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 事件实体  
* @author : KangNing Hu
*/
@TableName("sp_event")
public class EventEntity {

	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * 事件内容
	 */
	private String event;


	/**
	 * 有效时长
	 */
	private Long time;


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

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}
