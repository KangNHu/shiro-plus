package org.codingeasy.shiroplus.loader.admin.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.codingeasy.shiroplus.loader.admin.client.configuration.AdminServerProperties;

import java.net.InetAddress;

/**
* 安全相关的拦截器  
* @author : KangNing Hu
*/
public class SecurityRequestInterceptor implements RequestInterceptor {

	private static final String HEAD_TOKEN = "x-token";
	private static final String HEAD_IP= "x-forwarded-for";

	private AdminServerProperties adminServerProperties;


	public SecurityRequestInterceptor(AdminServerProperties properties){
		this.adminServerProperties = properties;
	}

	@Override
	public void apply(RequestTemplate requestTemplate) {
		//获取本机ip
		String ip = "未知";
		try {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
		}catch (Exception e){
			///
		}
		//获取的是本地的IP地址
		requestTemplate.header(HEAD_IP , ip);
		requestTemplate.header(HEAD_TOKEN , adminServerProperties.getToken());
	}

}
