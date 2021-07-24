package org.codingeasy.shiroplus.loader.admin.client.model;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventType;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;

/**
* 全局元数据变更事件传输对象
* @author : KangNing Hu
*/
public class GlobalMetadataEventDto extends AuthMetadataEvent {

	private GlobalMetadata globalMetadata;

	public GlobalMetadataEventDto(EventType type, Object metadata) {
		super(type, metadata);
	}


	public GlobalMetadataEventDto() {
		this(null , new Object());
	}

	public void setEventType(EventType type){
		this.type = type;
	}


	public GlobalMetadata getGlobalMetadata() {
		return globalMetadata;
	}

	public void setGlobalMetadata(GlobalMetadata globalMetadata) {
		this.globalMetadata = globalMetadata;
	}

	@Override
	public Object getSource() {
		return globalMetadata;
	}
}
