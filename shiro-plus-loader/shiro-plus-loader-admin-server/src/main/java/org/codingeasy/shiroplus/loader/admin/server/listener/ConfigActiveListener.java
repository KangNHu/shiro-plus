package org.codingeasy.shiroplus.loader.admin.server.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.event.Subscribe;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.server.dao.EventDao;
import org.codingeasy.shiroplus.loader.admin.server.dao.InstanceDao;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessAssert;
import org.codingeasy.shiroplus.loader.admin.server.models.dto.GlobalMetadataEventDto;
import org.codingeasy.shiroplus.loader.admin.server.models.dto.PermissionMetadataEventDto;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.*;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.ConfigType;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.Logical;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.PermissionModel;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.codingeasy.shiroplus.loader.admin.server.utils.JsonUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 配置活动个（变更） 监听器
 *
 * @author : KangNing Hu
 */
@Component
public class ConfigActiveListener {


	private final static Logger log = LoggerFactory.getLogger(ConfigActiveListener.class);

	@Autowired
	private EventDao eventDao;

	@Autowired
	@Lazy
	private ConfigService configService;

	@Autowired
	private InstanceDao instanceDao;

	@Subscribe
	public void onActive(ConfigEvent event) {
		if (log.isDebugEnabled()){
			log.debug("处理变更事件{}" , JsonUtils.toJsonString(event));
		}
		try {
			//处理全局元信息配置的变更事件
			processorGlobalConfigEvent(event);
			//处理权限元信息配置的变更事件
			processorPermissionConfigEvent(event);
		}catch (Exception e){
			log.error("处理元信息配置变更事件失败" , e);
		}
	}

	/**
	 * 处理权限元信息配置的变更事件
	 * @param event 事件对象
	 */
	private void processorPermissionConfigEvent(ConfigEvent event) {
		ConfigType configType = event.getConfigType();
		if (configType != ConfigType.PERMISSION) {
			return;
		}
		PermissionConfigEntity permissionConfig = (PermissionConfigEntity) event.getSource();
		//构建元信息
		PermissionMetadata permissionMetadata = permissionConfig.toMetadata();
		//构建shiro plus事件对象
		AuthMetadataEvent authMetadataEvent = new AuthMetadataEvent(event.getEventType(), permissionMetadata);
		saveEvent(authMetadataEvent);

	}
	/**
	 * 处理全局元信息配置的变更事件
	 * @param event 事件对象
	 */
	private void processorGlobalConfigEvent(ConfigEvent event) {
		ConfigType configType = event.getConfigType();
		if (configType != ConfigType.GLOBAL) {
			return;
		}
		GlobalConfigEntity globalConfig = (GlobalConfigEntity) event.getSource();
		BusinessAssert.notNull(globalConfig.getTenantId() ,"处理变更事件失败 ，租户ID不能为空");
		//构建元信息
		GlobalMetadata globalMetadata = globalConfig.toMetadata();
		//构建shiro plus事件对象
		AuthMetadataEvent authMetadataEvent = new AuthMetadataEvent(event.getEventType(), globalMetadata);
		saveEvent(authMetadataEvent);
	}

	/**
	 * 保存事件
	 * @param authMetadataEvent 事件对象
	 */
	private void saveEvent(AuthMetadataEvent authMetadataEvent){
		int sourceType;
		Object source = authMetadataEvent.getSource();
		if (source instanceof PermissionMetadata){
			PermissionMetadataEventDto permissionMetadataEventDto = new PermissionMetadataEventDto();
			permissionMetadataEventDto.setEventType(authMetadataEvent.getType());
			permissionMetadataEventDto.setPermissionMetadata((PermissionMetadata) authMetadataEvent.getSource());
			authMetadataEvent = permissionMetadataEventDto;
			sourceType =1;
		}else if (source instanceof GlobalMetadata){
			GlobalMetadataEventDto globalMetadataEventDto = new GlobalMetadataEventDto();
			globalMetadataEventDto.setEventType(authMetadataEvent.getType());
			globalMetadataEventDto.setGlobalMetadata((GlobalMetadata) authMetadataEvent.getSource());
			authMetadataEvent = globalMetadataEventDto;
			sourceType = 2;
		}else {
			log.warn("未知的事件源类型 {}" ,source.getClass());
			return;
		}
		String event = JsonUtils.toJsonString(authMetadataEvent);
		//获取所有存活的客户端实例
		List<InstanceEntity> instanceEntities = instanceDao.selectList(new QueryWrapper<>());
		if (!CollectionUtils.isEmpty(instanceEntities)) {
			eventDao.batchInsert(
					instanceEntities
							.stream()
							.map(item -> new EventEntity(event, item.getId(), sourceType))
							.collect(Collectors.toList())
			);
		}
	}

}

