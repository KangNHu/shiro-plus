package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.ConfigExtendEntity;

import java.util.List;

/**
* 扩展信息  
* @author : KangNing Hu
*/
@Mapper
public interface ConfigExtendDao extends BaseMapper<ConfigExtendEntity> {

	/**
	 * 批量插入
	 * @param configExtendEntities  待插入数据
	 * @return 影响行数
	 */
	int batchInsert(@Param("configExtendEntities") List<ConfigExtendEntity> configExtendEntities);
}
