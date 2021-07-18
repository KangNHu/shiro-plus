package org.codingeasy.shiroplus.loader.admin.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.codingeasy.shiroplus.loader.admin.client.configuration.AdminServerProperties;

/**
* 安全相关的拦截器  
* @author : KangNing Hu
*/
public class SecurityRequestInterceptor implements RequestInterceptor {

	private static final String HEAD_TOKEN = "x-token";

	private AdminServerProperties adminServerProperties;


	public SecurityRequestInterceptor(AdminServerProperties properties){
		this.adminServerProperties = properties;
	}

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header(HEAD_TOKEN , adminServerProperties.getToken());
	}
}
