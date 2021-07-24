package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigExtendEntity;

/**
 * 全局配置
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@Mapper
public interface GlobalConfigDao extends BaseMapper<GlobalConfigEntity> {

	/**
	 * 查询列表 （包括扩展字段）
	 * @param queryPage
	 * @return
	 */
	Page<GlobalConfigExtendEntity> queryList(Page<GlobalConfigExtendEntity> queryPage);
}