package org.codingeasy.shiroplus.loader.admin.server.service;

import org.codingeasy.shiroplus.loader.admin.server.models.Log;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.InstanceEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.LogsEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.SystemEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.InstanceRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.LogsRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.RequestPage;

import javax.servlet.http.HttpServletRequest;

public interface SystemService {
	/**
	 * 获取系统配置信息
	 * @return 返回系统配置信息
	 */
	SystemEntity get();

	/**
	 * 修改系统配置
	 * @param systemEntity 请求对象
	 * @return 返回响应对象
	 */
	int update(SystemEntity systemEntity);


	/**
	 * 日志条件分页
	 * @param request 查询条件
	 * @return
	 */
	Page<Log> logsPage(LogsRequest request);

	/**
	 * 心跳
	 * @param instanceCode 实例编码
	 * @return
	 */
	int ping(String instanceCode);


	/**
	 * 创建实例
	 * @param instanceCode 实例编码
	 * @return
	 */
	int createInstance(String instanceCode ,String applicationName , HttpServletRequest request);

	/**
	 * 分页查询
	 * @param request 查询条件
	 * @return
	 */
	Page<InstanceEntity> instancePage(InstanceRequest request);


	/**
	 * 删除实例id
	 * @param instanceEntity
	 * @return
	 */
	int deleteInstance(InstanceEntity instanceEntity );
}
