package org.codingeasy.shiroplus.nacos.metadata;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventType;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;

/**
* 基于nacos的全局元信息  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class NacosGlobalMetadata extends GlobalMetadata implements NacosMetadata {
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
