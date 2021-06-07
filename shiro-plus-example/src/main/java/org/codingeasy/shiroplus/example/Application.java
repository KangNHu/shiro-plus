package org.codingeasy.shiroplus.example;

import org.codingeasy.shiroplus.springboot.annotaion.EnableShiroPlus;
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
