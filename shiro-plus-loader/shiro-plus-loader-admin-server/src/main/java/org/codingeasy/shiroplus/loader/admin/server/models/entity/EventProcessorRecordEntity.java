package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 事件处理记录  
* @author : KangNing Hu
*/
@TableName("sp_event_processor_record")
public class EventProcessorRecordEntity {

	@TableId(type = IdType.AUTO)
	private Long id;


	/**
	 * 远程主机地址
	 */
	private String host;


	/**
	 * 事件id
	 */
	private Long eventId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
}
