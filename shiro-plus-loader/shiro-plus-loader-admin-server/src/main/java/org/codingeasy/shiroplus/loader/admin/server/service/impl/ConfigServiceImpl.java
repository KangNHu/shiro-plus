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
	private InstanceDao instanceDao;


	@Autowired
	private ConfigExtendDao configExtendDao;


	@Autowired
	private OpenApiDao openApiDao;

	@Autowired
	private EventManager eventManager;

	/**
	 * 获取所有全局配置
	 * @return 返回所有的全局配置信息
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
	 * 获取所有权限配置
	 * @return 返回所有的权限配置信息
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
	 * 拉取当前请求待消费的事件
	 * @return 返回所有待消费的事件
	 */
	@Override
	public AuthMetadataEventWrap pullEvents(String instanceCode) {
		//获取实例id
		InstanceEntity instanceEntity = instanceDao.selectOne(
				new QueryWrapper<InstanceEntity>()
						.lambda()
						.eq(InstanceEntity::getCode, instanceCode)
		);
		BusinessAssert.notNull(instanceEntity , "当前客户端实例未注册");
		//获取待消费事件
		List<EventEntity> events = eventDao.selectList(
				new QueryWrapper<EventEntity>()
						.lambda()
						.eq(EventEntity::getInstanceId, instanceEntity.getId())
		);
		if (!CollectionUtils.isEmpty(events)) {
			//删除已消费事件
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
		associationExtend(globalConfigEntity.getId() , globalConfigEntity.getExtend() ,ConfigType.constant(GLOBAL));
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
						.eq(ConfigExtendEntity::getType , ConfigType.constant(GLOBAL))
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
		if (globalConfigEntity != null) {
			globalConfigEntity.setExtend(getExtend(id, ConfigType.constant(GLOBAL)));
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
		associationExtend(globalConfigEntity.getId() , globalConfigEntity.getExtend() ,ConfigType.constant(GLOBAL));
		//发送更新事件
		eventManager.asyncPublish(ConfigEvent.globalUpdateEvent(globalConfigEntity));
		return 1;
	}

	/**
	 * 权限元数据条件分页列表
	 * @param request 请求条件
	 * @return 返回符合条件的权限配置列表
	 */
	@Override
	public Page<PermissionConfigEntity> permissionPage(PermissionConfigRequest request) {
		//创建分页条件
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<PermissionConfigEntity> query = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		query.setSize(request.getPageSize());
		query.setCurrent(request.getPageNo());
		//创建查询条件
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
			"'新增权限元信息[path:'" +
			" + #permissionConfigEntity.path " +
			"+',method:'+T(org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod).form(#permissionConfigEntity.method)"+
			"+']'"
	)
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int addPermission(@Search @Param(LogsProducer.NEW_VALUE_KEY) PermissionConfigEntity permissionConfigEntity) {
		//新增主表数据
		permissionConfigEntity.setCreateBy(UserUtils.getUserId());
		permissionConfigEntity.setUpdateBy(UserUtils.getUserId());
		permissionConfigEntity.setStatus(constant(NORMAL));
		permissionConfigEntity.setCreateTm(System.currentTimeMillis());
		permissionConfigDao.insert(permissionConfigEntity);
		//重新关联扩展字段
		associationExtend(permissionConfigEntity.getId() , permissionConfigEntity.getExtend() , ConfigType.constant(PERMISSION));
		//发送新增事件
		eventManager.asyncPublish(ConfigEvent.permissionAddEvent(permissionConfigEntity));
		return 1;
	}



	@Record("'删除权限元数据[ID:' + #id +']'")
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int deletePermission(Long id) {
		//查询旧数据
		PermissionConfigEntity permissionConfigEntity = permissionConfigDao.selectById(id);
		BusinessAssert.notNull(permissionConfigEntity , "未知的权限元信息");
		//删除主表信息
		permissionConfigDao.deleteById(id);
		//删除扩展信息
		configExtendDao.delete(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId , id)
						.eq(ConfigExtendEntity::getType , ConfigType.constant(PERMISSION))
		);
		//发送删除事件
		eventManager.asyncPublish(ConfigEvent.permissionDeleteEvent(permissionConfigEntity));
		return 0;
	}


	/**
	 * 获取权限元信息
	 * @param id 元信息id
	 * @return
	 */
	@Override
	public PermissionConfigEntity getPermission(Long id) {
		//获取基本数据
		PermissionConfigEntity permissionConfigEntity = permissionConfigDao.selectById(id);
		//获取扩展字段信息
		if (permissionConfigEntity != null) {
			permissionConfigEntity.setExtend(getExtend(id, ConfigType.constant(PERMISSION)));
		}
		return permissionConfigEntity;
	}


	@Record(
			"'更新权限元信息[path:'" +
			" + #permissionConfigEntity.path " +
			"+',method:'+T(org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod).form(#permissionConfigEntity.method)"+
			"+']'"
	)
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int updatePermission(@Search @Param(LogsProducer.NEW_VALUE_KEY) PermissionConfigEntity permissionConfigEntity) {
		//更新基本数据
		permissionConfigEntity.setUpdateBy(UserUtils.getUserId());
		permissionConfigDao.updateById(permissionConfigEntity);
		//重新关联
		associationExtend(permissionConfigEntity.getId() , permissionConfigEntity.getExtend() , ConfigType.constant(PERMISSION));
		//发送更新事件
		eventManager.asyncPublish(ConfigEvent.permissionUpdateEvent(permissionConfigEntity));
		return 1;
	}


	@Record("导入Api")
	@Override
	public int importApi(MultipartFile multipartFile) {
		Map<String , Object> map;
		try {
			InputStream inputStream = multipartFile.getInputStream();
			String json = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
			ObjectMapper objectMapper = new ObjectMapper();
			map = objectMapper.readValue(json, Map.class);
		}catch (Exception e){
			throw new BusinessException("导入文件格式错误" ,e);
		}
		//获取根路径
		String rootPath = (String) map.get(KEY_BASE_PATH);
		if (rootPath == null){
			throw new BusinessException("导入文件格式缺少根路径");
		}
		//构建api对象列表
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
		//批量插入
		if (!CollectionUtils.isEmpty(openApiEntities)) {
			openApiDao.batchInsert(openApiEntities);
		}
		return 1;
	}

	/**
	 * api接口列表分页
	 * @param request 请对象
	 * @return 返回分页数据
	 */
	@Override
	public Page<OpenApiEntity> apiPage(OpenAPiRequest request) {
		//创建分页条件
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<OpenApiEntity> query = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		query.setSize(request.getPageSize());
		query.setCurrent(request.getPageNo());
		//创建查询条件
		LambdaQueryWrapper<OpenApiEntity> queryWrapper = new QueryWrapper<OpenApiEntity>()
				.lambda()
				.eq(request.getMethod() != null , OpenApiEntity::getMethod , request.getMethod())
				.like(!StringUtils.isEmpty(request.getPath()), OpenApiEntity::getPath, request.getPath());
		return new Page<>(openApiDao.selectPage(query , queryWrapper));
	}

	/**
	 * 删除api
	 * @param id id
	 * @return
	 */
	@Record("'删除API[path:'+#id.path+',method:'+#id.method+']' + #id.summary")
	@Override
	public int deleteOpenApi(OpenApiEntity id) {
		//查询
		OpenApiEntity openApiEntity = openApiDao.selectById(id.getId());
		BusinessAssert.notNull(openApiEntity , "删除的api不存在");
		id.setMethod(openApiEntity.getMethod());
		id.setPath(openApiEntity.getPath());
		id.setSummary(openApiEntity.getSummary());
		return openApiDao.deleteById(id.getId());
	}




	/**
	 * 用于模糊查询列表
	 * @param path  接口路径
	 * @return 返回列表
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
	 * 关联扩展字段
	 * @param id 元数据id
	 * @param extend 扩展字段
	 * @param type 元信息类型
	 */
	private void associationExtend(Long id, Map<String, Object> extend , int type) {
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
					configExtendEntity.setType(type);
					return configExtendEntity;
				}).collect(Collectors.toList()));
	}

	/**
	 * 获取扩展信息
	 * @param id 元信息id
	 * @param type 元信息类型
	 */
	private Map<String, Object> getExtend(Long id, int type) {
		List<ConfigExtendEntity> configExtendEntities = configExtendDao.selectList(
				new QueryWrapper<ConfigExtendEntity>()
						.lambda()
						.eq(ConfigExtendEntity::getConfigId, id)
						.eq(ConfigExtendEntity::getType , type)
		);
		//装配扩展字段信息
		return Optional
				.ofNullable(configExtendEntities)
				.orElse(new ArrayList<>())
				.stream()
				.collect(Collectors.toMap(ConfigExtendEntity::getName , ConfigExtendEntity::getValue));

	}
}
