package org.codingeasy.gateway;

import org.codingeasy.shiroplus.nacos.annotation.EnableShiroPlusNacos;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
*   
* @author : KangNing Hu
*/
@SpringBootApplication
@EnableShiroPlusNacos
@EnableDiscoveryClient
public class GatewayApplication {


	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class , args);
	}


}
