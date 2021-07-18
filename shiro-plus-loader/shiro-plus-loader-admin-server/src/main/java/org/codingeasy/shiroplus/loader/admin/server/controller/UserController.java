package org.codingeasy.shiroplus.loader.admin.server.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.LogsEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PasswordInfoRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.RequestPage;
import org.codingeasy.shiroplus.loader.admin.server.models.request.UserPageRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
* 用户管理  
* @author : KangNing Hu
*/
@RestController
@RequestMapping("/admin/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 用户列表分页
	 * @param request 分页条件
	 * @return 响应分页数据
	 */
	@PostMapping("/page")
	public Response<Page<UserEntity>> page(@RequestBody UserPageRequest request){
		return Response.ok(userService.page(request));
	}

	/**
	 * 获取当前用户信息
	 * @return 响应当前用户信息
	 */
	@GetMapping("/userInfo")
	public Response<UserEntity> getCurrentUserInfo(){
		Subject subject = SecurityUtils.getSubject();
		return Response.ok(userService.getUser((Long) subject.getPrincipal()));
	}


	/**
	 * 获取当前用户的登录日志
	 * @return
	 */
	@PostMapping("/getCurrentUserLogs")
	public Response<Page<LogsEntity>> getCurrentUserLogs(@RequestBody RequestPage page){
		return Response.ok(userService.getCurrentUserLogs(page));
	}

	/**
	 * 获取详情
	 * @param userId 用户id
	 * @return 返回响应用户信息
	 */
	@GetMapping("")
	public Response<UserEntity> get(@NotNull(message = "用户id不能为空") Long userId){
		return Response.ok(userService.getUser(userId));
	}


	/**
	 * 删除用户
	 * @param userId 用户id
	 * @return
	 */
	@DeleteMapping("")
	public Response<Integer> delete(@NotNull(message = "用户id不能为空") Long userId){
		return Response.ok(userService.delete(userId));
	}


	/**
	 * 修改密码
	 * @param request 密码信息
	 * @return
	 */
	@PutMapping("/updatePassword")
	public Response<Integer> updatePassword(@RequestBody @Validated PasswordInfoRequest request){
		return Response.ok(userService.updatePassword(request));
	}


	/**
	 * 修改用户信息
	 * @param user 用户名称
	 * @return
	 */
	@PutMapping("")
	public Response<Integer> update(@RequestBody @Validated UserEntity user){
		return Response.ok(userService.update(user));
	}


	/**
	 * 新增用户信息
	 * @param user
	 * @return
	 */
	@PostMapping("")
	public Response<Integer> add(@RequestBody @Validated UserEntity user){
		return Response.ok(userService.add(user));
	}
}
