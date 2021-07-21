package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.loader.admin.server.dao.RoleDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.UserDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.UserRoleDao;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.RoleEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRolesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.CommonStatus;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.PermissionService;
import org.codingeasy.streamrecord.core.annotation.Param;
import org.codingeasy.streamrecord.core.annotation.Record;
import org.codingeasy.streamrecord.core.annotation.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer.NEW_VALUE_KEY;
import static org.codingeasy.shiroplus.loader.admin.server.models.menu.CommonStatus.*;

/**
* 权限管理  
* @author : KangNing Hu
*/
@RecordService
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private UserDao userDao;

	@Override
	public Page<UserRolesEntity> page(PermissionRequest request) {
		//设置分页条件
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserEntity> queryPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		queryPage.setCurrent(request.getPageNo());
		queryPage.setSize(request.getPageSize());
		//设置查询条件
		LambdaQueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>()
				.lambda()
				.like(!StringUtils.isEmpty(request.getUsername()), UserEntity::getUsername, request.getUsername())
				.in(UserEntity::getStatus, Arrays.asList(constant(NORMAL), constant(DISABLE)))
				.orderByDesc(UserEntity::getCreateTm);
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserEntity> resultPage = userDao.selectPage(queryPage, queryWrapper);
		List<UserEntity> records = resultPage.getRecords();
		if (CollectionUtils.isEmpty(records)){
			return new Page<>();
		}
		//查询用户角色列表
		PermissionRequest permissionRequest = new PermissionRequest();
		permissionRequest.setUserIds(
				records
						.stream()
						.map(UserEntity::getId)
						.collect(Collectors.toList())
		);
		List<UserRolesEntity> roles = roleDao.selectUserRoles(permissionRequest);
		//分组集合
		Map<Long, UserRolesEntity> map = roles.stream().collect(Collectors.toMap(UserRolesEntity::getId, Function.identity()));
		Page<UserRolesEntity> page = new Page<>();
		page.setTotal(resultPage.getTotal());
		page.setList(resultPage
				.getRecords()
				.stream()
				.map(UserEntity::getId)
				.map(map::get)
				.collect(Collectors.toList())
		);
		return page;
	}

	/**
	 * 详情
	 * @param userId 用户id
	 * @return  返回响应结果  用户名称 和 角色列表
	 */
	@Override
	public UserRolesEntity get(Long userId) {
		PermissionRequest permissionRequest = new PermissionRequest();
		permissionRequest.setUserId(userId);
		return Optional
				.of(roleDao.selectUserRoles(permissionRequest))
				.orElse(Arrays.asList(new UserRolesEntity()))
				.get(0);
	}


	/**
	 * 修改用户角色
	 * @param userRolesEntity 用户角色列表信息
	 * @return 响应修改结果
	 */
	@Record("'修改用户[' + #userRolesEntity.username + ']权限'")
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public int update(@Param(NEW_VALUE_KEY) UserRolesEntity userRolesEntity) {
		//删除已关联数据
		userRoleDao.delete(
				new QueryWrapper<UserRoleEntity>()
						.lambda()
						.eq(UserRoleEntity::getUserId , userRolesEntity.getId())

		);
		//重新关联数据
		List<RoleEntity> roles = userRolesEntity.getRoles();
		if (!CollectionUtils.isEmpty(roles)){
			userRoleDao.batchInsert(
					roles
						.stream()
						.map(item ->{
							UserRoleEntity userRole = new UserRoleEntity();
							userRole.setRoleId(item.getId());
							userRole.setUserId(userRolesEntity.getId());
							return userRole;
						}).collect(Collectors.toList()));
		}
		return 1;
	}

	/**
	 * 获取角色列表
	 * @return
	 */
	@Override
	public List<RoleEntity> getRoles() {
		return roleDao.selectList(new QueryWrapper<>());
	}
}
