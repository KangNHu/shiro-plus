package org.codingeasy.shiroplus.loader.admin.client.configuration;

import feign.Feign;
import feign.jackson.JacksonEncoder;
import okhttp3.OkHttpClient;
import org.codingeasy.shiroplus.loader.admin.client.AdminClient;
import org.codingeasy.shiroplus.loader.admin.client.ResponseDecoder;
import org.codingeasy.shiroplus.loader.admin.client.SecurityRequestInterceptor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
* 客户端配置  
* @author : KangNing Hu
*/
public class ClientConfiguration {


	/**
	 * 注册 feign builder对象
	 * @param properties 配置
	 * @return
	 */
	@Bean
	public  Feign.Builder feignBuilder(AdminServerProperties properties){
		return Feign.builder()
				.decoder(new ResponseDecoder())
				.encoder(new JacksonEncoder())
				.client(new feign.okhttp.OkHttpClient(new OkHttpClient
						.Builder()
						.connectTimeout(10 , TimeUnit.SECONDS)
						.callTimeout(10 , TimeUnit.SECONDS)
						.build()
				))
				.requestInterceptor(new SecurityRequestInterceptor(properties));
	}


	/**
	 * 构建admin 调用客户端
	 * @param builder builder对象
	 * @param properties 配置
	 * @return
	 */
	@Bean
	public AdminClient adminClient(Feign.Builder builder , AdminServerProperties properties){
		return builder.target(AdminClient.class , properties.getHost());
	}

}
