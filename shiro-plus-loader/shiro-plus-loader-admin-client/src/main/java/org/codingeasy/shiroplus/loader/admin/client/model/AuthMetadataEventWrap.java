package org.codingeasy.shiroplus.loader.admin.client.model;

import java.util.List;

/**
* 元数据事件包装类  
* @author : KangNing Hu
*/
public class AuthMetadataEventWrap {

	private List<GlobalMetadataEventDto> globalMetadataEvents;


	private List<PermissionMetadataEventDto> permissionMetadataEvents;


	public List<GlobalMetadataEventDto> getGlobalMetadataEvents() {
		return globalMetadataEvents;
	}

	public void setGlobalMetadataEvents(List<GlobalMetadataEventDto> globalMetadataEvents) {
		this.globalMetadataEvents = globalMetadataEvents;
	}

	public List<PermissionMetadataEventDto> getPermissionMetadataEvents() {
		return permissionMetadataEvents;
	}

	public void setPermissionMetadataEvents(List<PermissionMetadataEventDto> permissionMetadataEvents) {
		this.permissionMetadataEvents = permissionMetadataEvents;
	}
}
