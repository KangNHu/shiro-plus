package org.codingeasy.shiroplus.loader.admin.server.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.event.Subscribe;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.server.dao.EventDao;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessAssert;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
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
		PermissionConfigExtendEntity permissionConfig = (PermissionConfigExtendEntity) event.getSource();
		//构建元信息
		PermissionMetadata permissionMetadata = new PermissionMetadata();
		String permis = permissionConfig.getPermis();
		if (!StringUtils.isEmpty(permis)){
			permissionMetadata.setPermis(Arrays.asList(permis.split(",")));
		}
		permissionMetadata.setPermiModel(PermiModel.form(PermissionModel.form(permissionConfig.getPermiModel())));
		permissionMetadata.setLogical(org.codingeasy.shiroplus.core.metadata.Logical.form(Logical.form(permissionConfig.getLogical())));
		permissionMetadata.setMethod(org.codingeasy.shiroplus.core.metadata.RequestMethod.form(RequestMethod.form(permissionConfig.getMethod())));
		permissionMetadata.setAttr(Optional
				.of(permissionConfig.getExtendList())
				.orElse(new ArrayList<>())
				.stream()
				.collect(Collectors.toMap(ConfigExtendEntity::getName , ConfigExtendEntity::getValue))
		);
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
		GlobalMetadata globalMetadata = new GlobalMetadata();
		globalMetadata.setTenantId(globalConfig.getTenantId());
		globalMetadata.setEnableAuthorization(globalConfig.getEnableAuthorization() == 1);
		globalMetadata.setEnableAuthentication(globalConfig.getEnableAuthentication() == 1);
		String anons = globalConfig.getAnons();
		if (!StringUtils.isEmpty(anons)){
			globalMetadata.setAnons(Arrays.asList(anons.split(",")));
		}
		globalMetadata.setAttr(Optional
				.of(globalConfig.getExtend())
				.orElse(new HashMap<>())
		);
		//构建shiro plus事件对象
		AuthMetadataEvent authMetadataEvent = new AuthMetadataEvent(event.getEventType(), globalMetadata);
		saveEvent(authMetadataEvent);
	}

	/**
	 * 保存事件
	 * @param authMetadataEvent 事件对象
	 */
	private void saveEvent(AuthMetadataEvent authMetadataEvent){
		EventEntity eventEntity = new EventEntity();
		eventEntity.setEvent(JsonUtils.toJsonString(authMetadataEvent));
		eventEntity.setTime(SystemUtils.getEventTime());
		eventDao.insert(eventEntity);
	}

}

