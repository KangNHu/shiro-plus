package org.codingeasy.shiroplus.example;

import com.alibaba.nacos.api.annotation.NacosProperties;
import org.codingeasy.shiroplus.nacos.annotation.EnableShiroPlusNacos;
import org.codingeasy.shiroplus.springboot.annotaion.EnableShiroPlus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
*   
* @author : KangNing Hu
*/
@EnableShiroPlus
@SpringBootApplication
@EnableShiroPlusNacos(@NacosProperties(serverAddr = "localhost:8848"))
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class , args);
	}
}
