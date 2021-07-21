package org.codingeasy.shiroplus.loader.admin.server.service;


import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.UserSimple;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.LogsEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleCodesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PasswordInfoRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.RequestPage;
import org.codingeasy.shiroplus.loader.admin.server.models.request.UserPageRequest;
import org.codingeasy.shiroplus.loader.admin.server.security.LoginInfoToken;

import java.util.List;

/**
*   
* @author : KangNing Hu
*/
public interface UserService {

	/**
	 * 获取用户以及权限信息
	 * @param userId user id
	 * @return 返回用户权限信息 ，如果没有则返回null
	 */
	UserRoleCodesEntity getUserPermissions(Long userId);

	/**
	 * 获取用户基本信息
	 * @param username 用户名称 登录名称
	 * @return 返回用户信息
	 */
	UserEntity getUser(String username);


	/**
	 * 获取用户基本信息
	 * @param userId 用户id
	 * @return 返回用户信息
	 */
	UserEntity getUser(Long userId);


	/**
	 * 获取用户基本信息
	 * @param userId 用户id
	 * @param showPassword 是否展示密码
	 * @return 返回用户信息
	 */
	UserEntity getUser(Long userId , boolean showPassword);


	/**
	 * 用户登录
	 * @param loginInfoToken 等来了信息token
	 * @return 返回jwt 访问令牌
	 */
	String login(LoginInfoToken loginInfoToken);

	/**
	 * 用户列表分页
	 * @param request 分页条件
	 * @return 分页数据
	 */
	Page<UserEntity> page(UserPageRequest request);

	/**
	 * 删除用户
	 * @param userId 用户id
	 * @return
	 */
	int delete(Long userId);

	/**
	 * 修改用户密码
	 * @param request 密码信息
	 * @return
	 */
	int updatePassword(PasswordInfoRequest request);

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	int update(UserEntity user);

	/**
	 * 新增用户信息
	 * @param user
	 * @return
	 */
	int add(UserEntity user);

	/**
	 * 获取当前用户的登录日志
	 * @param page 分页参数
	 * @return 返回分页结果
	 */
	Page<LogsEntity> getCurrentUserLogs(RequestPage page);

	/**
	 * 重置密码
	 * @param user 用户
	 * @return
	 */
	int restPassword(UserEntity user);



	/**
	 * 获取用户下拉框数据
	 * @param username 搜索条件
	 * @return 返回列表
	 */
	List<UserSimple> getUserSelectData(String username);

}
