package org.codingeasy.shiroplus.loader.admin.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.EventEntity;

import java.util.List;

/**
*
 *   事件
* @author : KangNing Hu
*/
@Mapper
public interface EventDao extends BaseMapper<EventEntity> {
	/**
	 * pull 出待处理的事件列表
	 * @param remoteIp 远程ip
	 * @return 返回事件列表
	 */
	List<EventEntity> pullEvents(String remoteIp);
}
