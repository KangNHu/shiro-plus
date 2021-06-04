package org.codingeasy.shiro.example;

import org.codingeasy.shiro.springboot.annotaion.EnableShiroPlus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
*   
* @author : KangNing Hu
*/
@EnableShiroPlus
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class , args);
	}
}
