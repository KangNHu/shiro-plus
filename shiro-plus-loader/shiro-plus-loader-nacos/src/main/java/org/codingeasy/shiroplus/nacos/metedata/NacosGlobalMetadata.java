package org.codingeasy.shiroplus.nacos.metedata;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;

/**
* 基于nacos的全局元信息  
* @author : KangNing Hu
*/
public class NacosGlobalMetadata extends GlobalMetadata implements NacosMetadata {
	/**
	 * 事件类型
	 */
	private AuthMetadataEvent.EventType eventType;


	public AuthMetadataEvent.EventType getEventType() {
		return eventType;
	}

	public void setEventType(AuthMetadataEvent.EventType eventType) {
		this.eventType = eventType;
	}
}
