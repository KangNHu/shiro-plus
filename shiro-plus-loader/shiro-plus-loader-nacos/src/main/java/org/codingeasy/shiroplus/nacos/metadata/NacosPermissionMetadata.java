package org.codingeasy.shiroplus.nacos.metadata;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventType;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;

/**
* 基于nacos的权限元信息  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class NacosPermissionMetadata extends PermissionMetadata implements NacosMetadata{

	/**
	 * 事件类型
	 */
	private EventType eventType;


	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
}
