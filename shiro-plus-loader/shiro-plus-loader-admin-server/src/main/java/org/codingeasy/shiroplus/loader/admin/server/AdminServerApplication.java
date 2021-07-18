package org.codingeasy.shiroplus.loader.admin.server;

import org.codingeasy.shiroplus.springboot.annotaion.EnableShiroPlus;
import org.codingeasy.streamrecord.springboot.annotation.EnableStreamRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* 启动类  
* @author : KangNing Hu
*/
@SpringBootApplication
@EnableShiroPlus
@EnableStreamRecord
public class AdminServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminServerApplication.class , args);
	}


}
