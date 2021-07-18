package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.SystemEntity;

/**
* 系统  
* @author : KangNing Hu
*/
@Mapper
public interface SystemDao extends BaseMapper<SystemEntity> {
}
