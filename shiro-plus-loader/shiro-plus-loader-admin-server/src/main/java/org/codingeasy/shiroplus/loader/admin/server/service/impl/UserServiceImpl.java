package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.codingeasy.shiroplus.loader.admin.server.dao.LogsDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.UserDao;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessAssert;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessException;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.shiroplus.loader.admin.server.logs.route.LoginRouteTarget;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.UserSimple;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.LogsEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleCodesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.BusinessCode;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PasswordInfoRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.RequestPage;
import org.codingeasy.shiroplus.loader.admin.server.models.request.UserPageRequest;
import org.codingeasy.shiroplus.loader.admin.server.security.LoginInfoToken;
import org.codingeasy.shiroplus.loader.admin.server.service.UserService;
import org.codingeasy.shiroplus.loader.admin.server.utils.EncryptionUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.JwtUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.SystemUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.UserUtils;
import org.codingeasy.streamrecord.core.AttributeAccess;
import org.codingeasy.streamrecord.core.annotation.*;
import org.codingeasy.streamrecord.core.processor.ProcessorStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer.BUSINESS_ID_KEY;
import static org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer.NEW_VALUE_KEY;
import static org.codingeasy.shiroplus.loader.admin.server.models.menu.CommonStatus.*;

/**
* 用户管理业务层
* @author : KangNing Hu
*/
@RecordService
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private LogsDao logsDao;

	/**
	 * 获取用户以及权限信息
	 * @param userId user id
	 * @return 返回用户权限信息 ，如果没有则返回null
	 */
	@Override
	public UserRoleCodesEntity getUserPermissions(Long userId) {
		return userDao.getUserPermissions(userId);
	}

	/**
	 * 获取用户基本信息
	 * @param username 用户名称 登录名称
	 * @return 返回用户信息
	 */
	@Override
	public UserEntity getUser(@NotNull String username) {
		return userDao.selectOne(new QueryWrapper<UserEntity>()
				.lambda()
				.eq(UserEntity::getUsername , username)
				.in(UserEntity::getStatus , Arrays.asList(constant(NORMAL), constant(DISABLE)))
		);
	}
	/**
	 * 获取用户基本信息
	 * @param userId 用户id
	 * @return 返回用户信息
	 */
	@Override
	public UserEntity getUser(Long userId) {
		return getUser(userId , false);
	}


	/**
	 * 获取用户基本信息
	 * @param userId 用户id
	 * @param showPassword 是否展示密码
	 * @return 返回用户信息
	 */
	@Override
	public UserEntity getUser(Long userId , boolean showPassword) {
		UserEntity userEntity = userDao.selectById(userId);
		if (!showPassword) {
			userEntity.setPassword(null);
		}
		return userEntity;
	}

	/**
	 * 用户登录
	 * @param loginInfoToken 等来了信息token
	 * @return 返回jwt 访问令牌
	 */
	@Override
	@Record(value = "login", strategy =ProcessorStrategy.ROUTE_NAME)
	@RouteTarget(value = LoginRouteTarget.class ,paramTypes = {AttributeAccess.class})
	public String login(LoginInfoToken loginInfoToken) {
		UserEntity user = this.getUser(loginInfoToken.getUsername());
		if (user == null){
			throw new AuthenticationException("账户不存在");
		}
		if (user.getStatus() ==constant(DISABLE)){
			throw new AuthenticationException("账户被禁用");
		}
		if (user.getId() == null){
			throw new AuthenticationException("未知的用户");
		}
		//设置主体
		loginInfoToken.setUserId(user.getId());
		//校验密码
		if (!EncryptionUtils.verify(user.getPassword() , loginInfoToken.getPasswordStr() , loginInfoToken.getUsername())){
			throw new AuthenticationException("密码错误");
		}
		return JwtUtils.createToken(user.getId() ,loginInfoToken.getRequest() );
	}


	/**
	 * 用户列表分页
	 * @param request 分页条件
	 * @return 分页数据
	 */
	@Override
	public Page<UserEntity> page(UserPageRequest request) {
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserEntity> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		page.setCurrent(request.getPageNo());
		page.setSize(request.getPageSize());
		page.setOrders(Arrays.asList(OrderItem.desc("last_update_tm")));
		Page<UserEntity> userPage = new Page<>(userDao.selectPage(page, new QueryWrapper<UserEntity>()
				.lambda()
				.like(!StringUtils.isEmpty(request.getUsername()), UserEntity::getUsername, request.getUsername())
				.like(!StringUtils.isEmpty(request.getNickname()), UserEntity::getNickname, request.getNickname())
				.in(UserEntity::getStatus, Arrays.asList(DISABLE.getValue(), NORMAL.getValue()))
		));
		Optional
				.of(userPage.getList())
				.orElse(new ArrayList<>())
				.forEach(item -> item.setPassword(null));
		return userPage;
	}

	/**
	 * 删除用户
	 * @param userId 用户id
	 * @return
	 */
	@Record("'删除用户 ID:'+#userId+''")
	@Override
	public int delete(@Param(BUSINESS_ID_KEY) Long userId) {
		return userDao.deleteById(userId);
	}

	/**
	 * 修改用户密码
	 * @param request 密码信息
	 * @return
	 */
	@Record("修改密码")
	@Override
	public int updatePassword(@Search PasswordInfoRequest request) {
		UserEntity user = getUser(request.getUserId() , true);
		if (user == null){
			throw new BusinessException("账户不存在");
		}
		if (!EncryptionUtils.verify(user.getPassword() , request.getOldPassword() , user.getUsername())){
			throw new BusinessException("旧密码错误");
		}
		BusinessAssert.state(!request.getNewPassword().equals(request.getOldPassword()) , "新旧密码不能一样");
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setPassword(EncryptionUtils.encrypt(request.getNewPassword() , user.getUsername()));
		userEntity.setUpdateBy((Long) SecurityUtils.getSubject().getPrincipal());
		return userDao.updateById(userEntity);
	}

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@Record("'更新用户['+#user.username +']'")
	@Override
	public int update(@Search @Param(NEW_VALUE_KEY) UserEntity user) {
		user.setPassword(null);
		return userDao.updateById(user);
	}

	/**
	 * 新增用户信息
	 * @param user
	 * @return
	 */
	@Override
	public int add(UserEntity user) {

		UserEntity old = getUser(user.getUsername());
		BusinessAssert.isNull(old , "用户名称已存在");
		Long userId = UserUtils.getUserId();
		user.setUpdateBy(userId);
		user.setCreateBy(userId);
		user.setCreateTm(System.currentTimeMillis());
		user.setStatus(constant(NORMAL));
		return userDao.insert(user);
	}


	/**
	 * 获取当前登录人的登录日志
	 * @param request 请求对象
	 * @return
	 */
	@Override
	public Page<LogsEntity> getCurrentUserLogs(RequestPage request) {
		//分页条件
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<LogsEntity> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		page.setCurrent(request.getPageNo());
		page.setSize(request.getPageSize());
		//查询条件
		LambdaQueryWrapper<LogsEntity> queryWrapper = new QueryWrapper<LogsEntity>()
				.lambda()
				.eq(LogsEntity::getBusinessCode, BusinessCode.constant(BusinessCode.LOGIN))
				.eq(LogsEntity::getBusinessId, UserUtils.getUserId())
				.orderByDesc(LogsEntity::getCreateTm);
		return new Page<>(logsDao.selectPage(page , queryWrapper));
	}


	/**
	 * 重置密码
	 * @param userId 用户id
	 * @return
	 */
	@Record("'重置了用户[' + #user.username + ']密码'")
	@Override
	public int restPassword(@Search UserEntity user) {
		UserEntity userEntity = userDao.selectById(user.getId());
		BusinessAssert.notNull(userEntity.getId() , "用户不存在");
		//用于日志模版参数
		user.setUsername(userEntity.getUsername());
		userEntity = new UserEntity();
		userEntity.setUpdateBy(UserUtils.getUserId());
		userEntity.setId(user.getId());
		userEntity.setPassword(EncryptionUtils.encrypt(SystemUtils.getInitPassword() , user.getUsername()));
		return userDao.updateById(userEntity);
	}



	/**
	 * 获取用户下拉框数据
	 * @return 返回列表
	 */
	@Override
	public List<UserSimple> getUserSelectData(String username) {
		LambdaQueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>()
				.lambda()
				.like(!StringUtils.isEmpty(username), UserEntity::getUsername, username)
				.in(UserEntity::getStatus, Arrays.asList(constant(NORMAL), constant(DISABLE)));
		List<UserEntity> userEntities = userDao.selectList(queryWrapper);
		return userEntities
				.stream()
				.map(item ->{
					UserSimple userSimple = new UserSimple();
					userSimple.setId(item.getId());
					userSimple.setUsername(item.getUsername());
					return userSimple;
				}).collect(Collectors.toList());
	}


}
