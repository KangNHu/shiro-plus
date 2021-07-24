package org.codingeasy.gateway.admin;

import org.codingeasy.shiroplus.loader.admin.client.annotation.EnableShiroPlusAdminClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
*   
* @author : KangNing Hu
*/
@SpringBootApplication
@EnableShiroPlusAdminClient
@EnableDiscoveryClient
public class GatewayApplication {


	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class , args);
	}


}
