package org.codingeasy.shiroplus.nacos.metedata;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;

/**
* 基于nacos的权限元信息  
* @author : KangNing Hu
*/
public class NacosPermissionMetadata extends PermissionMetadata implements NacosMetadata{

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
