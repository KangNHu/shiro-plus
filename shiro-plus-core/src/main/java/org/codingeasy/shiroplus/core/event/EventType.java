package org.codingeasy.shiroplus.core.event;

/**
 * 事件类型
 *
 * @author : kangning <a>2035711178@qq.com</a>
 */
public enum EventType {
	/**
	 * 更新事件
	 */
	UPDATE,
	/**
	 * 删除事件
	 */
	DELETE,
	/**
	 * 新增事件
	 */
	ADD,
	/**
	 * 清除事件，用于全部清除
	 */
	CLEAN
}