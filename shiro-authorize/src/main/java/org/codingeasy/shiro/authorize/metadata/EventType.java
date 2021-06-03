package org.codingeasy.shiro.authorize.metadata;

/**
* 事件类型  
* @author : KangNing Hu
*/
public enum  EventType {
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
