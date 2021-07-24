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
	protected EventType type;



	public AuthMetadataEvent(EventType type , Object metadata){
		super(metadata);
		this.type = type;
	}




	public EventType getType() {
		return type;
	}
}
