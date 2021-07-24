package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.InstanceEntity;

import java.util.List;

/**
* 事件处理记录  
* @author : KangNing Hu
*/
@Mapper
public interface InstanceDao extends BaseMapper<InstanceEntity> {

}
