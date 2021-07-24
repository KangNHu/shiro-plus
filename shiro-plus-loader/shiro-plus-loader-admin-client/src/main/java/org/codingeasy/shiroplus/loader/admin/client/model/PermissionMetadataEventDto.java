package org.codingeasy.shiroplus.loader.admin.client.model;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventType;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;

/**
* 权限元数据变更事件传输对象  
* @author : KangNing Hu
*/
public class PermissionMetadataEventDto extends AuthMetadataEvent {

	private PermissionMetadata permissionMetadata;

	public PermissionMetadataEventDto(EventType type, Object metadata) {
		super(type, metadata);
	}


	public PermissionMetadataEventDto() {
		this(null , new Object());
	}

	public void setEventType(EventType type){
		this.type = type;
	}


	public PermissionMetadata getPermissionMetadata() {
		return permissionMetadata;
	}

	public void setPermissionMetadata(PermissionMetadata permissionMetadata) {
		this.permissionMetadata = permissionMetadata;
	}

	@Override
	public Object getSource() {
		return permissionMetadata;
	}
}
