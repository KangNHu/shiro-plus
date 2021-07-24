package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigExtendEntity;

import java.util.List;

/**
 * 权限配置表
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@Mapper
public interface PermissionConfigDao extends BaseMapper<PermissionConfigEntity> {

	/**
	 * 分页查询列表 （包括扩展字段列表信息）
	 * @param page
	 * @return
	 */
	Page<PermissionConfigExtendEntity> queryList(Page<PermissionConfigExtendEntity> page);
}