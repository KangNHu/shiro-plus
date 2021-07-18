package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.EventProcessorRecordEntity;

import java.util.List;

/**
* 事件处理记录  
* @author : KangNing Hu
*/
@Mapper
public interface EventProcessorRecordDao extends BaseMapper<EventProcessorRecordEntity> {

	/**
	 * 批量插入
	 * @param entities 插入实体列表
	 * @return 返回影响行数
	 */
	int batchInsert(@Param("entities") List<EventProcessorRecordEntity> entities);
}
