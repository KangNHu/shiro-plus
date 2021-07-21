package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleCodesEntity;

/**
 * 用户表
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

	/**
	 * 获取用户以及权限信息
	 * @param userId user id
	 * @return 返回用户权限信息 ，如果没有则返回null
	 */
	UserRoleCodesEntity getUserPermissions(@Param("userId") Long userId);
}