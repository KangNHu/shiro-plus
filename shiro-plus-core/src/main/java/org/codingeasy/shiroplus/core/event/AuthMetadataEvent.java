package org.codingeasy.shiroplus.core.event;

import java.util.EventObject;

/**
* 权限元数据事件
* @author : kangning <a>2035711178@qq.com</a>
*/
public class AuthMetadataEvent extends EventObject {

	/**
	 * 事件类型
	 */
	private EventType type;



	public AuthMetadataEvent(EventType type , Object metadata){
		super(metadata);
		this.type = type;
	}


	public EventType getType() {
		return type;
	}


	/**
	 * 事件类型
	 * @author : kangning <a>2035711178@qq.com</a>
	 */
	public  enum  EventType {
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
}
