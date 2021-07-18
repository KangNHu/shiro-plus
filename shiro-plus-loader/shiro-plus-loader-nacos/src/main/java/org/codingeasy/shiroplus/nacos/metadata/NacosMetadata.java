package org.codingeasy.shiroplus.nacos.metadata;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventType;

/**
 * nacos 元信息标示
 * @author kangning <a>2035711178@qq.com</a>
 */
public interface NacosMetadata {

	/**
	 * 获取事件类型
	 * @return 返回事件类型
	 */
	EventType getEventType();

	/**
	 * 设置事件类型
	 * @param eventType 事件类型对象
	 */
	void setEventType(EventType eventType);
}
