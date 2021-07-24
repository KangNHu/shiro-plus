package org.codingeasy.shiroplus.loader.admin.client.metadata;

import org.apache.commons.collections.CollectionUtils;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.MetadataLoader;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.client.AdminClient;
import org.codingeasy.shiroplus.loader.admin.client.configuration.AdminServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
* shiro plus admin的加载器  
* @author : KangNing Hu
*/
public class ShiroPlusAdminClientMetadataLoader implements   MetadataLoader {



	private AdminClient adminClient;


	public ShiroPlusAdminClientMetadataLoader(AdminClient adminClient){
		this.adminClient = adminClient;
	}

	@Override
	public List<PermissionMetadata> load() {
		return adminClient.getPermissionMetadataAll().getData();
	}

	@Override
	public List<GlobalMetadata> loadGlobal() {
		return adminClient.getGlobalMetadataAll().getData();
	}



}
