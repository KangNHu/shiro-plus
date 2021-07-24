package org.codingeasy.shiroplus.loader.admin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nimbusds.jose.jwk.RSAKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.loader.admin.server.dao.*;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessAssert;
import org.codingeasy.shiroplus.loader.admin.server.exception.BusinessException;
import org.codingeasy.shiroplus.loader.admin.server.models.Log;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.*;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.BusinessCode;
import org.codingeasy.shiroplus.loader.admin.server.models.request.InstanceRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.LogsRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.RequestPage;
import org.codingeasy.shiroplus.loader.admin.server.service.SystemService;
import org.codingeasy.shiroplus.loader.admin.server.utils.JwtUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.SystemUtils;
import org.codingeasy.shiroplus.loader.admin.server.utils.WebUtils;
import org.codingeasy.streamrecord.core.annotation.Param;
import org.codingeasy.streamrecord.core.annotation.Record;
import org.codingeasy.streamrecord.core.annotation.RecordService;
import org.codingeasy.streamrecord.core.annotation.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.codingeasy.shiroplus.loader.admin.server.logs.LogsProducer.NEW_VALUE_KEY;

/**
* 系统管理  
* @author : KangNing Hu
*/
@RecordService
public class SystemServiceImpl  implements SystemService {

	private final static Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);

	@Autowired
	private SystemDao systemDao;

	@Autowired
	private LogsDao logsDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private InstanceDao instanceDao;


	@Autowired
	private EventDao eventDao;
	/**
	 * 获取系统配置信息
	 * @return 返回系统配置信息
	 */
	@Override
	public SystemEntity get() {
		return SystemUtils.getSource().selectById(SystemUtils.getVersion());
	}

	/**
	 * 修改系统配置
	 * @param systemEntity 请求对象
	 * @return 返回响应对象
	 */
	@Record("修改系统全局配置")
	@Override
	public int update(@Param(NEW_VALUE_KEY) SystemEntity systemEntity) {
		String keyPair = systemEntity.getKeyPair();
		//校验密钥的合法性
		if (!StringUtils.isEmpty(keyPair)){
			try {
				JwtUtils.validateRSAKey(keyPair);
			}catch (Exception e){
				throw new BusinessException("登录token的签名密钥无效或无法被解析");
			}
		}
		systemEntity.setVersion(SystemEntity.VERSION);
		return systemDao.updateById(systemEntity);
	}



	/**
	 * 日志条件分页
	 * @param request 查询条件
	 * @return
	 */
	@Override
	public Page<Log> logsPage(LogsRequest request) {
		//分页条件
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<LogsEntity> query = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		query.setCurrent(request.getPageNo());
		query.setSize(request.getPageSize());
		//查询条件
		LambdaQueryWrapper<LogsEntity> queryWrapper = new QueryWrapper<LogsEntity>()
				.lambda()
				.notIn(LogsEntity::getBusinessCode , Arrays.asList(BusinessCode.constant(BusinessCode.LOGIN)))
				.eq(request.getOperateId() != null, LogsEntity::getOperationId, request.getOperateId())
				.eq(request.getBusinessCode() != null, LogsEntity::getBusinessCode, request.getBusinessCode())
				.orderByDesc(LogsEntity::getCreateTm);
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<LogsEntity> page = logsDao.selectPage(query, queryWrapper);
		List<LogsEntity> records = page.getRecords();
		//获取操作人信息
		Map<Long ,String> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(records)){
			map.putAll(
					userDao.selectBatchIds(
					records
							.stream()
							.map(LogsEntity::getOperationId)
							.distinct()
							.collect(Collectors.toList())
					).stream()
					 .collect(Collectors.toMap(UserEntity::getId , UserEntity::getUsername))
			);
		}
		//构建返回对象
		Page<Log> logPage = new Page<>();
		logPage.setTotal(page.getTotal());
		logPage.setTotal(page.getTotal());
		logPage.setList(
				Optional
						.ofNullable(records)
						.orElse(new ArrayList<>())
						.stream()
						.map(item ->{
							Log log = new Log();
							log.setId(item.getId());
							log.setContext(item.getContext());
							log.setOperateName(map.get(item.getOperationId()));
							log.setCreateTime(item.getCreateTm());
							return log;
						}).collect(Collectors.toList())
		);
		return logPage;
	}


	/**
	 * 心跳
	 * @param instanceCode 实例编码
	 * @return
	 */
	@Override
	public int ping(String instanceCode) {
		logger.info("接受到客户端的心跳 code : {}" , instanceCode);
		// update entity
		InstanceEntity instanceEntity = new InstanceEntity();
		instanceEntity.setLastPingTime(System.currentTimeMillis());
		//update condition
		LambdaQueryWrapper<InstanceEntity> query = new QueryWrapper<InstanceEntity>()
				.lambda()
				.eq(InstanceEntity::getCode, instanceCode);
		return instanceDao.update(instanceEntity , query);
	}

	/**
	 * 创建实例
	 * @param instanceCode 实例编码
	 * @param applicationName 应用名称
	 * @return
	 */
	@Override
	public int createInstance(String instanceCode ,String applicationName ,HttpServletRequest request) {
		String remoteIp = WebUtils.getRemoteIp(request);
		logger.info("客户端创建实例 applicationName:{} IP: {}" , applicationName ,remoteIp);
		InstanceEntity instanceEntity = new InstanceEntity();
		long timeMillis = System.currentTimeMillis();
		instanceEntity.setLastPingTime(timeMillis);
		instanceEntity.setCode(instanceCode);
		instanceEntity.setName(applicationName);
		instanceEntity.setIp(remoteIp);
		instanceEntity.setCreateTm(timeMillis);
		return instanceDao.insert(instanceEntity);
	}

	/**
	 * 分页查询
	 * @param request 查询条件
	 * @return
	 */
	public Page<InstanceEntity> instancePage(InstanceRequest request){
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<InstanceEntity> query = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
		query.setCurrent(request.getPageNo());
		query.setSize(request.getPageSize());
		//设置查询条件
		LambdaQueryWrapper<InstanceEntity> queryWrapper = new QueryWrapper<InstanceEntity>()
				.lambda()
				.like(!StringUtils.isEmpty(request.getName()), InstanceEntity::getName, request.getName());
		return new Page<>(instanceDao.selectPage(query , queryWrapper));
	}

	/**
	 * 删除实例
	 * @param instanceEntity
	 * @return
	 */
	@Record("'删除客户端实例[应用名:' + #instanceEntity.name + ',IP:' +#instanceEntity.ip + ']'")
	@Transactional(rollbackFor = Throwable.class)
	@Override
	public int deleteInstance(InstanceEntity instanceEntity) {
		//查询实例
		InstanceEntity instance = instanceDao.selectById(instanceEntity.getId());
		BusinessAssert.notNull(instance , "实例不存在");
		instanceEntity.setName(instance.getName());
		instanceEntity.setIp(instance.getIp());
		//删除主表
		instanceDao.deleteById(instanceEntity.getId());
		//删除这个实例待消费的所有事件
		eventDao.delete(
				new QueryWrapper<EventEntity>()
					.lambda()
					.eq(EventEntity::getInstanceId , instanceEntity.getId())
		);
		return 1;
	}


}
