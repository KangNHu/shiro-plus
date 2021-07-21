package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.UserRoleEntity;

import java.util.List;

/**
* 用户角色中间表  
* @author : KangNing Hu
*/
@Mapper
public interface UserRoleDao extends BaseMapper<UserRoleEntity> {

	/**
	 * 批量插入
	 * @param userRoleEntities 用户角色信息
	 * @return 返回影响行数
	 */
	int batchInsert(@Param("userRoleEntities") List<UserRoleEntity> userRoleEntities);
}
