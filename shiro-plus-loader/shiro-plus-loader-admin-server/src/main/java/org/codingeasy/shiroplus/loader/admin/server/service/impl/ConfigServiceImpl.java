package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.loader.admin.server.dao.*;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessAssert;
import org.codingeasy.shiroplus.loader.admin.server.listener.ConfigEvent;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.*;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.CommonStatus;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.ConfigType;
import org.codingeasy.shiroplus.loader.admin.server.models.request.GlobalConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.codingeasy.shiroplus.loader.admin.server.utils.UserUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.WebUtils;
import org.codingeasy.streamrecord.core.annotation.Param;
import org.codingeasy.streamrecord.core.annotation.Record;
import org.codingeasy.streamrecord.core.annotation.RecordService;
import org.codingeasy.streamrecord.core.annotation.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.codingeasy.shiroplus.loader.admin.server.models.menu.CommonStatus.NORMAL;

/**
* 配置管理 业务层  
* @author : KangNing Hu
*/
@RecordService
public class ConfigServiceImpl implements ConfigService {

	@Autowired
	private GlobalConfigDao globalConfigDao;

	@Autowired
	private PermissionConfigDao permissionConfigDao;

	@Autowired
	private EventDao eventDao;

	@Autowired
	private EventProcessorRecordDao eventProcessorRecordDao;


	@Autowired
	private ConfigExtendDao configExtendDao;

	@Autowired
	private EventManager eventManager;

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
	public Page<GlobalConfigEntity> globalPage(GlobalConfigRequest request) {
		//设置分页条件
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<GlobalConfigEntity> query = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		query.setCurrent(request.getPageNo());
		query.setSize(request.getPageSize());
		//设置查询条件
		LambdaQueryWrapper<GlobalConfigEntity> queryWrapper = new QueryWrapper<GlobalConfigEntity>()
				.lambda()
				.eq(!StringUtils.isEmpty(request.getTenantId() ), GlobalConfigEntity::getTenantId, request.getTenantId())
				.eq(GlobalConfigEntity::getStatus, CommonStatus.constant(NORMAL))
				.orderByDesc(GlobalConfigEntity::getCreateTm);
		return new Page<>(globalConfigDao.selectPage(query ,queryWrapper));
	}


	/**
	 * 新增全局元信息
	 * @param globalConfigEntity 待新增的数据
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Throwable.class)
	@Record("'新增全局元信息[租户ID:'+#globalConfigEntity.tenantId+']'")
	public int addGlobal(GlobalConfigEntity globalConfigEntity) {
		Integer count = globalConfigDao.selectCount(
				new QueryWrapper<GlobalConfigEntity>()
						.lambda()
						.eq(GlobalConfigEntity::getTenantId, globalConfigEntity.getTenantId())
		);
		BusinessAssert.state(count == 0 , "租户已存在");
		globalConfigEntity.setCreateBy(UserUtils.getUserId());
		globalConfigEntity.setUpdateBy(UserUtils.getUserId());
		globalConfigEntity.setCreateTm(System.currentTimeMillis());
		globalConfigEntity.setStatus(CommonStatus.constant(NORMAL));
		globalConfigDao.insert(globalConfigEntity);
		//关联扩展字段
		associationExtend(globalConfigEntity.getId() , globalConfigEntity.getExtend());
		//发送新增事件
		eventManager.asyncPublish(ConfigEvent.globalAddEvent(globalConfigEntity));
		return 1;
	}

	/**
	 * 删除全局元信息
	 * @param id 元信息id
	 * @return
	 */
	@Record("'删除全局元信息[ID:'+#id+']'")
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public int deleteGlobal(@Param(LogsProducer.BUSINESS_ID_KEY) Long id) {
		//获取
		GlobalConfigEntity globalConfigEntity = globalConfigDao.selectById(id);
		//删除主表数据
		globalConfigDao.deleteById(id);
		//删除扩展信息
		configExtendDao.delete(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId , id)
		);
		//发送删除事件
		eventManager.asyncPublish(ConfigEvent.globalDeleteEvent(globalConfigEntity));
		return 1;
	}

	/**
	 * 获取全局元信息
	 * @param id 元信息id
	 * @return
	 */
	@Override
	public GlobalConfigEntity getGlobal(Long id) {
		//获取基本数据
		GlobalConfigEntity globalConfigEntity = globalConfigDao.selectById(id);
		//获取扩展字段信息
		List<ConfigExtendEntity> configExtendEntities = configExtendDao.selectList(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId, id)
		);
		//装配扩展字段信息
		if (!CollectionUtils.isEmpty(configExtendEntities)){
			globalConfigEntity.setExtend(configExtendEntities.stream().collect(Collectors.toMap(ConfigExtendEntity::getName , ConfigExtendEntity::getValue)));
		}
		return globalConfigEntity;
	}


	/**
	 * 修改全局元信息
	 * @param globalConfigEntity 全局元信息
	 * @return
	 */
	@Record("'修改全局元信息[tenantID:'+ #globalConfigEntity.tenantId +']'")
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int updateGlobal(@Search @Param(LogsProducer.NEW_VALUE_KEY) GlobalConfigEntity globalConfigEntity) {
		//校验租户是否存在
		Integer count = globalConfigDao.selectCount(
				new QueryWrapper<GlobalConfigEntity>()
						.lambda()
						.notIn(GlobalConfigEntity::getId, globalConfigEntity.getId())
						.eq(GlobalConfigEntity::getTenantId, globalConfigEntity.getTenantId())
		);
		BusinessAssert.state(count == 0 , "租户ID已存在");
		//修改主表信息
		globalConfigEntity.setUpdateBy(UserUtils.getUserId());
		globalConfigDao.updateById(globalConfigEntity);
		//重新关联扩展字段
		associationExtend(globalConfigEntity.getId() , globalConfigEntity.getExtend());
		//发送更新事件
		eventManager.asyncPublish(ConfigEvent.globalUpdateEvent(globalConfigEntity));
		return 1;
	}

	/**
	 * 关联扩展字段
	 * @param id 元数据id
	 * @param extend 扩展字段
	 */
	private void associationExtend(Long id, Map<String, Object> extend) {
		//删除旧的关联
		configExtendDao.delete(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId , id)
		);
		//重新关联
		if (MapUtils.isEmpty(extend)){
			return;
		}
		configExtendDao.batchInsert(extend
				.entrySet()
				.stream()
				.map(item ->{
					ConfigExtendEntity configExtendEntity = new ConfigExtendEntity();
					configExtendEntity.setConfigId(id);
					configExtendEntity.setName(item.getKey());
					configExtendEntity.setValue(item.getValue() == null ? null :item.getValue().toString());
					configExtendEntity.setType(ConfigType.constant(ConfigType.GLOBAL));
					return configExtendEntity;
				}).collect(Collectors.toList()));
	}
}
