package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.checkerframework.checker.units.qual.A;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.loader.admin.server.dao.EventDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.EventProcessorRecordDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.GlobalConfigDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.PermissionConfigDao;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.EventEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.EventProcessorRecordEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.GlobalConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.codingeasy.shiroplus.loader.admin.server.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* 配置管理 业务层  
* @author : KangNing Hu
*/
@Service
public class ConfigServiceImpl implements ConfigService {

	@Autowired
	private GlobalConfigDao globalConfigDao;

	@Autowired
	private PermissionConfigDao permissionConfigDao;

	@Autowired
	private EventDao eventDao;

	@Autowired
	private EventProcessorRecordDao eventProcessorRecordDao;

	/**
	 * 获取所有全局配置
	 * @return 返回所有的全局配置信息
	 */
	@Override
	public List<GlobalConfigEntity> getGlobalConfigs() {
		int pageNo = 1;
		List<GlobalConfigEntity> globalConfigEntities = new ArrayList<>();
		List<GlobalConfigEntity> currentGlobalConfigEntities = null;
		do {
			com.baomidou.mybatisplus.extension.plugins.pagination.Page<GlobalConfigEntity> queryPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
			queryPage.setSize(2000);
			queryPage.setCurrent(pageNo);
			Page<GlobalConfigEntity> page = new Page<>(globalConfigDao.selectPage(queryPage, new QueryWrapper<GlobalConfigEntity>().lambda()));
			currentGlobalConfigEntities = page.getList();
			globalConfigEntities.addAll(currentGlobalConfigEntities);
			pageNo++;
		}while (!CollectionUtils.isEmpty(currentGlobalConfigEntities));
		return globalConfigEntities;
	}

	/**
	 * 获取所有权限配置
	 * @return 返回所有的权限配置信息
	 */
	@Override
	public List<PermissionConfigEntity> getPermissionConfigs() {
		int pageNo = 1;
		List<PermissionConfigEntity> permissionConfigEntities = new ArrayList<>();
		List<PermissionConfigEntity> currentGlobalConfigEntities = null;
		do {
			com.baomidou.mybatisplus.extension.plugins.pagination.Page<PermissionConfigEntity> queryPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
			queryPage.setSize(2000);
			queryPage.setCurrent(pageNo);
			Page<PermissionConfigEntity> page = new Page<>(permissionConfigDao.selectPage(queryPage, new QueryWrapper<PermissionConfigEntity>().lambda()));
			currentGlobalConfigEntities = page.getList();
			permissionConfigEntities.addAll(currentGlobalConfigEntities);
			pageNo++;
		}while (!CollectionUtils.isEmpty(currentGlobalConfigEntities));
		return permissionConfigEntities;
	}

	/**
	 * 拉取当前请求待消费的事件
	 * @return 返回所有待消费的事件
	 */
	@Override
	public List<AuthMetadataEvent> pullEvents(HttpServletRequest request) {
		//获取远程ip
		String remoteIp = WebUtils.getRemoteIp(request);
		//pull event list
		List<EventEntity> eventEntities = eventDao.pullEvents(remoteIp);
		if (CollectionUtils.isEmpty(eventEntities)) {
			return new ArrayList<>();
		}
		// 添加event 处理记录
		eventProcessorRecordDao.batchInsert(eventEntities
				.stream()
				.map(item -> {
					EventProcessorRecordEntity eventProcessorRecordEntity = new EventProcessorRecordEntity();
					eventProcessorRecordEntity.setEventId(item.getId());
					eventProcessorRecordEntity.setHost(remoteIp);
					return eventProcessorRecordEntity;
				})
				.collect(Collectors.toList()));
		ObjectMapper objectMapper = new ObjectMapper();
		//转换实体
		return eventEntities
				.stream()
				.map(item -> {
					try {
						return objectMapper.readValue(item.getEvent(), AuthMetadataEvent.class);
					} catch (JsonProcessingException e) {
						throw new IllegalStateException(e);
					}
				}).collect(Collectors.toList());

	}
	@Override
	public Page<PermissionConfigEntity> permissionPage(PermissionConfigRequest request) {

		return null;
	}

	@Override
	public Page<GlobalConfig> globalPage(GlobalConfigRequest request) {
		return null;
	}
}
