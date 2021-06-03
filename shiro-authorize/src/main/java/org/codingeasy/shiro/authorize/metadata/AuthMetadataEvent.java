package org.codingeasy.shiro.authorize.metadata;

/**
* 权限元信息事件  
* @author : KangNing Hu
*/
public class AuthMetadataEvent<T> {

	/**
	 * 事件类型
	 */
	private EventType type;

	/**
	 * 事件数据
	 */
	private T metadata;

	public AuthMetadataEvent(EventType type , T metadata){
		this.metadata = metadata;
		this.type = type;
	}

	public EventType getType() {
		return type;
	}

	public T getMetadata() {
		return metadata;
	}
}
