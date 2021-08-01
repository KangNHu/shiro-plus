package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.OpenApiEntity;

import java.util.List;

/**
 * api 接口
 * @author hukangning
 */
@Mapper
public interface OpenApiDao extends BaseMapper<OpenApiEntity> {

	//批量插入
	int batchInsert(@Param("openApiEntities") List<OpenApiEntity> openApiEntities);
}
