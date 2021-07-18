package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.ConfigExtendEntity;

/**
 * 配置扩展表
 *
 * @author chenbutao chenbutao@qipeipu.com
 * @since 1.0.0 2021-07-01
 */
@Mapper
public interface ConfigExtendDao extends BaseMapper<ConfigExtendEntity> {

}