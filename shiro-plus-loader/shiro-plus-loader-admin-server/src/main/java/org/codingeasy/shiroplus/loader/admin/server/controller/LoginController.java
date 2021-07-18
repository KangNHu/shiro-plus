package org.codingeasy.shiroplus.loader.admin.server.controller;

import org.codingeasy.shiroplus.loader.admin.server.models.LoginInfo;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.security.LoginInfoToken;
import org.codingeasy.shiroplus.loader.admin.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
*   
* @author : KangNing Hu
*/
@RestController
@RequestMapping("login")
public class LoginController {

	@Autowired
	private UserService userService;

	/**
	 * 账户密码登录
	 * @param loginInfo 登录信息
	 * @return 响应数据 token
	 */
	@RequestMapping
	public Response<String> login(@RequestBody LoginInfo loginInfo , HttpServletRequest request){
		return Response.<String>ok(userService.login(new LoginInfoToken(request ,loginInfo )));
	}



}
