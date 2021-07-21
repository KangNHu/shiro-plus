package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.RoleEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRolesEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionRequest;

import java.util.List;

/**
 *
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {

	/**
	 * 查询用户角色名称列表
	 * @param request 请求条件
	 * @return 返回角色名称列表
	 */
	List<UserRolesEntity> selectUserRoles(@Param("request") PermissionRequest request);

}