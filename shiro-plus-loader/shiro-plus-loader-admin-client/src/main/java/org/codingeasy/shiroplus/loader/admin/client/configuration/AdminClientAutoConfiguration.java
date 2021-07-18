package org.codingeasy.shiroplus.loader.admin.client.configuration;

import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.MetadataLoader;
import org.codingeasy.shiroplus.loader.admin.client.AdminClient;
import org.codingeasy.shiroplus.loader.admin.client.metadata.ShiroPlusAdminClientMetadataLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
* admin客户端自动装配类  
* @author : KangNing Hu
*/
@ConditionalOnClass(EventManager.class)
public class AdminClientAutoConfiguration {


	/**
	 * 注册admin 服务端相关的配置参数
	 * @return
	 */
	@ConfigurationProperties("shiroplus.admin.client")
	@Bean
	public AdminServerProperties serverProperties(){
		return new AdminServerProperties();
	}

	/**
	 * 注册admin client 元数据加载器
	 * @param eventManager 事件管理器
	 * @param adminClient admin 客服端
	 * @param adminServerProperties  admin 服务端相关的配置参数
	 * @return
	 */
	@Primary
	@Bean
	public MetadataLoader metadataLoader(EventManager eventManager ,
	                                     AdminClient adminClient,
	                                     AdminServerProperties adminServerProperties){
		ShiroPlusAdminClientMetadataLoader shiroPlusAdminClientMetadataLoader = new ShiroPlusAdminClientMetadataLoader(eventManager, adminClient);
		shiroPlusAdminClientMetadataLoader.setRefreshInterval(adminServerProperties.getRefreshInterval());
		return shiroPlusAdminClientMetadataLoader;
	}
}
