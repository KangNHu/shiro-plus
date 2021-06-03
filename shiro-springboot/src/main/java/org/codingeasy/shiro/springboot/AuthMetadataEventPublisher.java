package org.codingeasy.shiro.springboot;

import org.apache.shiro.mgt.CachingSecurityManager;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataEvent;

/**
* 权限元信息事件的发布器
 * <p>可发布 {@link org.codingeasy.shiro.authorize.metadata.PermissionMetadata}变更事件 </p>
 * <p>可发布 {@link org.codingeasy.shiro.authorize.metadata.GlobalMetadata}变更事件</p>
* @author : KangNing Hu
*/
public class AuthMetadataEventPublisher {


	private CachingSecurityManager cachingSecurityManager;


	public AuthMetadataEventPublisher(CachingSecurityManager cachingSecurityManager){
		this.cachingSecurityManager = cachingSecurityManager;
	}


	public <T>void publish(AuthMetadataEvent<T> authMetadataEvent){
		cachingSecurityManager.getEventBus().publish(authMetadataEvent);
	}

}
