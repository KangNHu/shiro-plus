package org.codingeasy.shiroplus.springboot;

import com.alibaba.nacos.api.annotation.NacosProperties;
import org.codingeasy.shiroplus.nacos.annotation.EnableShiroPlusNacos;
import org.codingeasy.shiroplus.springboot.annotaion.EnableShiroPlus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
*   
* @author : kangning <a>2035711178@qq.com</a>
*/
@EnableShiroPlus
@SpringBootApplication
@EnableShiroPlusNacos(@NacosProperties(serverAddr = "localhost:8848"))
public class SimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleApplication.class , args);
	}
}
