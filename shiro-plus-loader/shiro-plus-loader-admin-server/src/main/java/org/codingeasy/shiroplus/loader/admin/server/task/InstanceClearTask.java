package org.codingeasy.shiroplus.loader.admin.server.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.codingeasy.shiroplus.loader.admin.server.dao.EventDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.InstanceDao;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.EventEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.InstanceEntity;
import org.codingeasy.shiroplus.loader.admin.server.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* 客户端实例清除  
* @author : KangNing Hu
*/
@Component
public class InstanceClearTask {


	@Autowired
	private InstanceDao instanceDao;


	@Autowired
	private EventDao eventDao;

	@Scheduled(cron = "0 0 23 * * ?")
	@Transactional(rollbackFor = Throwable.class)
	public void clearDownLineClientInstance(){
		long heartbeatMaxTime = SystemUtils.getHeartbeatMaxTime();
		//当前时间
		long currentTime = System.currentTimeMillis();
		//计算临界时间
		long criticalTime = currentTime - heartbeatMaxTime;

		LambdaQueryWrapper<InstanceEntity> queryWrapper = new QueryWrapper<InstanceEntity>()
				.lambda()
				.lt(InstanceEntity::getLastPingTime, criticalTime);
		//获取客户端实例的最后活动时间小于临界时间的所有数据
		List<InstanceEntity> instanceEntities = instanceDao.selectList(queryWrapper);
		//删除主表数据
		instanceDao.delete(queryWrapper);
		//删除事件表数据
		if (!CollectionUtils.isEmpty(instanceEntities)){
			//分批 200 个一批
			Lists.partition(
					instanceEntities
							.stream()
							.map(InstanceEntity::getId)
							.collect(Collectors.toList()),
					200
			).stream()
			 .forEach(item ->
			 	eventDao.delete(
			 			new QueryWrapper<EventEntity>()
					        .lambda()
						    .in(EventEntity::getInstanceId , item)
			    )
			 );
		}

	}
}
