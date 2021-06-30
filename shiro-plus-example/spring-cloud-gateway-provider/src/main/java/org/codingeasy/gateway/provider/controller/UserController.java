package org.codingeasy.gateway.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
*   
* @author : KangNing Hu
*/
@RequestMapping("user")
@RestController
public class UserController {



	@GetMapping
	public String getUserId(){
		return "1234567";
	}
}
