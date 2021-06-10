package org.codingeasy.shiroplus.nacos.metedata;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;

/**
 * nacos 元信息标示
 * @author hukangning
 */
public interface NacosMetadata {

	/**
	 * 获取事件类型
	 * @return 返回事件类型
	 */
	AuthMetadataEvent.EventType getEventType();

	/**
	 * 设置事件类型
	 * @param eventType 事件类型对象
	 */
	void setEventType(AuthMetadataEvent.EventType eventType);
}
