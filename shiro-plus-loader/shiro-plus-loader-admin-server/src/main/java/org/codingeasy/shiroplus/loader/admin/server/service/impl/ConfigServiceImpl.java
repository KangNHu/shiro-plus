package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.server.dao.*;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessAssert;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessException;
import org.codingeasy.shiroplus.loader.admin.server.listener.ConfigEvent;
import org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.SwaggerConstant;
import org.codingeasy.shiroplus.loader.admin.server.models.dto.AuthMetadataEventWrap;
import org.codingeasy.shiroplus.loader.admin.server.models.dto.GlobalMetadataEventDto;
import org.codingeasy.shiroplus.loader.admin.server.models.dto.PermissionMetadataEventDto;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.*;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.CommonStatus;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.ConfigType;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod;
import org.codingeasy.shiroplus.loader.admin.server.models.request.GlobalConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.OpenAPiRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.service.ConfigService;
import org.codingeasy.shiroplus.loader.admin.server.utils.UserUtils;
import org.codingeasy.streamrecord.core.annotation.Param;
import org.codingeasy.streamrecord.core.annotation.Record;
import org.codingeasy.streamrecord.core.annotation.RecordService;
import org.codingeasy.streamrecord.core.annotation.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.codingeasy.shiroplus.loader.admin.server.models.SwaggerConstant.KEY_BASE_PATH;
import static org.codingeasy.shiroplus.loader.admin.server.models.SwaggerConstant.KEY_PATHS;
import static org.codingeasy.shiroplus.loader.admin.server.models.menu.CommonStatus.NORMAL;
import static org.codingeasy.shiroplus.loader.admin.server.models.menu.CommonStatus.constant;
import static org.codingeasy.shiroplus.loader.admin.server.models.menu.ConfigType.GLOBAL;
import static org.codingeasy.shiroplus.loader.admin.server.models.menu.ConfigType.PERMISSION;

/**
* ???????????? ?????????  
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
	private InstanceDao instanceDao;


	@Autowired
	private ConfigExtendDao configExtendDao;


	@Autowired
	private OpenApiDao openApiDao;

	@Autowired
	private EventManager eventManager;

	/**
	 * ????????????????????????
	 * @return ?????????????????????????????????
	 */
	@Override
	public List<GlobalMetadata> getGlobalMetadataAll() {
		int pageNo = 1;
		List<GlobalConfigExtendEntity> globalConfigEntities = new ArrayList<>();
		List<GlobalConfigExtendEntity> currentGlobalConfigEntities = null;
		do {
			com.baomidou.mybatisplus.extension.plugins.pagination.Page<GlobalConfigExtendEntity> queryPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
			queryPage.setSize(2000);
			queryPage.setCurrent(pageNo);
			Page<GlobalConfigExtendEntity> page = new Page<>(globalConfigDao.queryList(queryPage));
			currentGlobalConfigEntities = page.getList();
			globalConfigEntities.addAll(currentGlobalConfigEntities);
			pageNo++;
		}while (!CollectionUtils.isEmpty(currentGlobalConfigEntities));
		return globalConfigEntities
				.stream()
				.map(GlobalConfigExtendEntity::toMetadata)
				.collect(Collectors.toList());
	}

	/**
	 * ????????????????????????
	 * @return ?????????????????????????????????
	 */
	@Override
	public List<PermissionMetadata> getPermissionMetadataAll() {
		int pageNo = 1;
		List<PermissionConfigExtendEntity> permissionConfigEntities = new ArrayList<>();
		List<PermissionConfigExtendEntity> currentGlobalConfigEntities = null;
		do {
			com.baomidou.mybatisplus.extension.plugins.pagination.Page<PermissionConfigExtendEntity> queryPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
			queryPage.setSize(2000);
			queryPage.setCurrent(pageNo);
			Page<PermissionConfigExtendEntity> page = new Page<>(permissionConfigDao.queryList(queryPage));
			currentGlobalConfigEntities = page.getList();
			permissionConfigEntities.addAll(currentGlobalConfigEntities);
			pageNo++;
		}while (!CollectionUtils.isEmpty(currentGlobalConfigEntities));
		return permissionConfigEntities
				.stream()
				.map(PermissionConfigExtendEntity::toMetadata)
				.collect(Collectors.toList());
	}

	/**
	 * ????????????????????????????????????
	 * @return ??????????????????????????????
	 */
	@Override
	public AuthMetadataEventWrap pullEvents(String instanceCode) {
		//????????????id
		InstanceEntity instanceEntity = instanceDao.selectOne(
				new QueryWrapper<InstanceEntity>()
						.lambda()
						.eq(InstanceEntity::getCode, instanceCode)
		);
		BusinessAssert.notNull(instanceEntity , "??????????????????????????????");
		//?????????????????????
		List<EventEntity> events = eventDao.selectList(
				new QueryWrapper<EventEntity>()
						.lambda()
						.eq(EventEntity::getInstanceId, instanceEntity.getId())
		);
		if (!CollectionUtils.isEmpty(events)) {
			//?????????????????????
			eventDao.deleteBatchIds(
					events
							.stream()
							.distinct()
							.map(EventEntity::getId)
							.collect(Collectors.toList())
			);
		}
		AuthMetadataEventWrap authMetadataEventWrap = new AuthMetadataEventWrap();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			for (EventEntity event : events) {
				if (event.getSourceType() == ConfigType.constant(PERMISSION)) {
					authMetadataEventWrap.getPermissionMetadataEvents().add(objectMapper.readValue(event.getEvent(), PermissionMetadataEventDto.class));
				} else if (event.getSourceType() == ConfigType.constant(GLOBAL)) {
					authMetadataEventWrap.getGlobalMetadataEvents().add(objectMapper.readValue(event.getEvent(), GlobalMetadataEventDto.class));
				}
			}
		}catch (Exception e){
			throw new IllegalStateException(e);
		}
		return authMetadataEventWrap;

	}


	@Override
	public Page<GlobalConfigEntity> globalPage(GlobalConfigRequest request) {
		//??????????????????
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<GlobalConfigEntity> query = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		query.setCurrent(request.getPageNo());
		query.setSize(request.getPageSize());
		//??????????????????
		LambdaQueryWrapper<GlobalConfigEntity> queryWrapper = new QueryWrapper<GlobalConfigEntity>()
				.lambda()
				.eq(!StringUtils.isEmpty(request.getTenantId() ), GlobalConfigEntity::getTenantId, request.getTenantId())
				.eq(GlobalConfigEntity::getStatus, CommonStatus.constant(NORMAL))
				.orderByDesc(GlobalConfigEntity::getCreateTm);
		return new Page<>(globalConfigDao.selectPage(query ,queryWrapper));
	}


	/**
	 * ?????????????????????
	 * @param globalConfigEntity ??????????????????
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Throwable.class)
	@Record("'?????????????????????[??????ID:'+#globalConfigEntity.tenantId+']'")
	public int addGlobal(GlobalConfigEntity globalConfigEntity) {
		Integer count = globalConfigDao.selectCount(
				new QueryWrapper<GlobalConfigEntity>()
						.lambda()
						.eq(GlobalConfigEntity::getTenantId, globalConfigEntity.getTenantId())
		);
		BusinessAssert.state(count == 0 , "???????????????");
		globalConfigEntity.setCreateBy(UserUtils.getUserId());
		globalConfigEntity.setUpdateBy(UserUtils.getUserId());
		globalConfigEntity.setCreateTm(System.currentTimeMillis());
		globalConfigEntity.setStatus(CommonStatus.constant(NORMAL));
		globalConfigDao.insert(globalConfigEntity);
		//??????????????????
		associationExtend(globalConfigEntity.getId() , globalConfigEntity.getExtend() ,ConfigType.constant(GLOBAL));
		//??????????????????
		eventManager.asyncPublish(ConfigEvent.globalAddEvent(globalConfigEntity));
		return 1;
	}

	/**
	 * ?????????????????????
	 * @param id ?????????id
	 * @return
	 */
	@Record("'?????????????????????[ID:'+#id+']'")
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public int deleteGlobal(@Param(LogsProducer.BUSINESS_ID_KEY) Long id) {
		//??????
		GlobalConfigEntity globalConfigEntity = globalConfigDao.selectById(id);
		//??????????????????
		globalConfigDao.deleteById(id);
		//??????????????????
		configExtendDao.delete(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId , id)
						.eq(ConfigExtendEntity::getType , ConfigType.constant(GLOBAL))
		);
		//??????????????????
		eventManager.asyncPublish(ConfigEvent.globalDeleteEvent(globalConfigEntity));
		return 1;
	}

	/**
	 * ?????????????????????
	 * @param id ?????????id
	 * @return
	 */
	@Override
	public GlobalConfigEntity getGlobal(Long id) {
		//??????????????????
		GlobalConfigEntity globalConfigEntity = globalConfigDao.selectById(id);
		//????????????????????????
		if (globalConfigEntity != null) {
			globalConfigEntity.setExtend(getExtend(id, ConfigType.constant(GLOBAL)));
		}
		return globalConfigEntity;
	}




	/**
	 * ?????????????????????
	 * @param globalConfigEntity ???????????????
	 * @return
	 */
	@Record("'?????????????????????[tenantID:'+ #globalConfigEntity.tenantId +']'")
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int updateGlobal(@Search @Param(LogsProducer.NEW_VALUE_KEY) GlobalConfigEntity globalConfigEntity) {
		//????????????????????????
		Integer count = globalConfigDao.selectCount(
				new QueryWrapper<GlobalConfigEntity>()
						.lambda()
						.notIn(GlobalConfigEntity::getId, globalConfigEntity.getId())
						.eq(GlobalConfigEntity::getTenantId, globalConfigEntity.getTenantId())
		);
		BusinessAssert.state(count == 0 , "??????ID?????????");
		//??????????????????
		globalConfigEntity.setUpdateBy(UserUtils.getUserId());
		globalConfigDao.updateById(globalConfigEntity);
		//????????????????????????
		associationExtend(globalConfigEntity.getId() , globalConfigEntity.getExtend() ,ConfigType.constant(GLOBAL));
		//??????????????????
		eventManager.asyncPublish(ConfigEvent.globalUpdateEvent(globalConfigEntity));
		return 1;
	}

	/**
	 * ?????????????????????????????????
	 * @param request ????????????
	 * @return ???????????????????????????????????????
	 */
	@Override
	public Page<PermissionConfigEntity> permissionPage(PermissionConfigRequest request) {
		//??????????????????
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<PermissionConfigEntity> query = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		query.setSize(request.getPageSize());
		query.setCurrent(request.getPageNo());
		//??????????????????
		LambdaQueryWrapper<PermissionConfigEntity> queryWrapper = new QueryWrapper<PermissionConfigEntity>()
				.lambda()
				.eq(request.getMethod() != null, PermissionConfigEntity::getMethod, request.getMethod())
				.eq(request.getPermiModel() != null, PermissionConfigEntity::getPermiModel, request.getPermiModel())
				.like(!StringUtils.isEmpty(request.getPath()), PermissionConfigEntity::getPath, request.getPath())
				.like(StringUtils.isEmpty(request.getPermiCode()), PermissionConfigEntity::getPermis, request.getPermiCode())
				.orderByDesc(PermissionConfigEntity::getCreateTm);
		return new Page<>(permissionConfigDao.selectPage(query , queryWrapper));
	}

	@Record(
			"'?????????????????????[path:'" +
			" + #permissionConfigEntity.path " +
			"+',method:'+T(org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod).form(#permissionConfigEntity.method)"+
			"+']'"
	)
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int addPermission(@Search @Param(LogsProducer.NEW_VALUE_KEY) PermissionConfigEntity permissionConfigEntity) {
		//??????????????????
		permissionConfigEntity.setCreateBy(UserUtils.getUserId());
		permissionConfigEntity.setUpdateBy(UserUtils.getUserId());
		permissionConfigEntity.setStatus(constant(NORMAL));
		permissionConfigEntity.setCreateTm(System.currentTimeMillis());
		permissionConfigDao.insert(permissionConfigEntity);
		//????????????????????????
		associationExtend(permissionConfigEntity.getId() , permissionConfigEntity.getExtend() , ConfigType.constant(PERMISSION));
		//??????????????????
		eventManager.asyncPublish(ConfigEvent.permissionAddEvent(permissionConfigEntity));
		return 1;
	}



	@Record("'?????????????????????[ID:' + #id +']'")
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int deletePermission(Long id) {
		//???????????????
		PermissionConfigEntity permissionConfigEntity = permissionConfigDao.selectById(id);
		BusinessAssert.notNull(permissionConfigEntity , "????????????????????????");
		//??????????????????
		permissionConfigDao.deleteById(id);
		//??????????????????
		configExtendDao.delete(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId , id)
						.eq(ConfigExtendEntity::getType , ConfigType.constant(PERMISSION))
		);
		//??????????????????
		eventManager.asyncPublish(ConfigEvent.permissionDeleteEvent(permissionConfigEntity));
		return 0;
	}


	/**
	 * ?????????????????????
	 * @param id ?????????id
	 * @return
	 */
	@Override
	public PermissionConfigEntity getPermission(Long id) {
		//??????????????????
		PermissionConfigEntity permissionConfigEntity = permissionConfigDao.selectById(id);
		//????????????????????????
		if (permissionConfigEntity != null) {
			permissionConfigEntity.setExtend(getExtend(id, ConfigType.constant(PERMISSION)));
		}
		return permissionConfigEntity;
	}


	@Record(
			"'?????????????????????[path:'" +
			" + #permissionConfigEntity.path " +
			"+',method:'+T(org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod).form(#permissionConfigEntity.method)"+
			"+']'"
	)
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int updatePermission(@Search @Param(LogsProducer.NEW_VALUE_KEY) PermissionConfigEntity permissionConfigEntity) {
		//??????????????????
		permissionConfigEntity.setUpdateBy(UserUtils.getUserId());
		permissionConfigDao.updateById(permissionConfigEntity);
		//????????????
		associationExtend(permissionConfigEntity.getId() , permissionConfigEntity.getExtend() , ConfigType.constant(PERMISSION));
		//??????????????????
		eventManager.asyncPublish(ConfigEvent.permissionUpdateEvent(permissionConfigEntity));
		return 1;
	}


	@Record("??????Api")
	@Override
	public int importApi(MultipartFile multipartFile) {
		Map<String , Object> map;
		try {
			InputStream inputStream = multipartFile.getInputStream();
			String json = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
			ObjectMapper objectMapper = new ObjectMapper();
			map = objectMapper.readValue(json, Map.class);
		}catch (Exception e){
			throw new BusinessException("????????????????????????" ,e);
		}
		//???????????????
		String rootPath = (String) map.get(KEY_BASE_PATH);
		if (rootPath == null){
			throw new BusinessException("?????????????????????????????????");
		}
		//??????api????????????
		List<OpenApiEntity> openApiEntities = new ArrayList<>();
		Map<String , Map<String , Map<String ,Object>>> paths = (Map<String, Map<String, Map<String, Object>>>) map.get(KEY_PATHS);
		for (Map.Entry<String , Map<String , Map<String ,Object>>> entry : paths.entrySet()){
			String path = entry.getKey();
			path = rootPath + path;
			Map<String, Map<String, Object>> methods = entry.getValue();
			for (Map.Entry<String, Map<String, Object>> methodEntry : methods.entrySet()){
				String method = methodEntry.getKey();
				String summary = (String) methodEntry.getValue().get(SwaggerConstant.KEY_SUMMARY);
				openApiEntities.add(new OpenApiEntity(path , RequestMethod.form(method), summary));
			}
		}
		//????????????
		if (!CollectionUtils.isEmpty(openApiEntities)) {
			openApiDao.batchInsert(openApiEntities);
		}
		return 1;
	}

	/**
	 * api??????????????????
	 * @param request ?????????
	 * @return ??????????????????
	 */
	@Override
	public Page<OpenApiEntity> apiPage(OpenAPiRequest request) {
		//??????????????????
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<OpenApiEntity> query = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		query.setSize(request.getPageSize());
		query.setCurrent(request.getPageNo());
		//??????????????????
		LambdaQueryWrapper<OpenApiEntity> queryWrapper = new QueryWrapper<OpenApiEntity>()
				.lambda()
				.eq(request.getMethod() != null , OpenApiEntity::getMethod , request.getMethod())
				.like(!StringUtils.isEmpty(request.getPath()), OpenApiEntity::getPath, request.getPath());
		return new Page<>(openApiDao.selectPage(query , queryWrapper));
	}

	/**
	 * ??????api
	 * @param id id
	 * @return
	 */
	@Record("'??????API[path:'+#id.path+',method:'+#id.method+']' + #id.summary")
	@Override
	public int deleteOpenApi(OpenApiEntity id) {
		//??????
		OpenApiEntity openApiEntity = openApiDao.selectById(id.getId());
		BusinessAssert.notNull(openApiEntity , "?????????api?????????");
		id.setMethod(openApiEntity.getMethod());
		id.setPath(openApiEntity.getPath());
		id.setSummary(openApiEntity.getSummary());
		return openApiDao.deleteById(id.getId());
	}




	/**
	 * ????????????????????????
	 * @param path  ????????????
	 * @return ????????????
	 */
	@Override
	public List<OpenApiEntity> likeOpenApi(String path) {
		if (StringUtils.isEmpty(path)){
			return new ArrayList<>();
		}
		return openApiDao.selectList(
				new QueryWrapper<OpenApiEntity>()
						.lambda()
						.like(OpenApiEntity::getPath , path)
		);
	}

	/**
	 * ??????????????????
	 * @param id ?????????id
	 * @param extend ????????????
	 * @param type ???????????????
	 */
	private void associationExtend(Long id, Map<String, Object> extend , int type) {
		//??????????????????
		configExtendDao.delete(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId , id)
		);
		//????????????
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
					configExtendEntity.setType(type);
					return configExtendEntity;
				}).collect(Collectors.toList()));
	}

	/**
	 * ??????????????????
	 * @param id ?????????id
	 * @param type ???????????????
	 */
	private Map<String, Object> getExtend(Long id, int type) {
		List<ConfigExtendEntity> configExtendEntities = configExtendDao.selectList(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId, id)
						.eq(ConfigExtendEntity::getType , type)
		);
		//????????????????????????
		return Optional
				.ofNullable(configExtendEntities)
				.orElse(new ArrayList<>())
				.stream()
				.collect(Collectors.toMap(ConfigExtendEntity::getName , ConfigExtendEntity::getValue));

	}
}
