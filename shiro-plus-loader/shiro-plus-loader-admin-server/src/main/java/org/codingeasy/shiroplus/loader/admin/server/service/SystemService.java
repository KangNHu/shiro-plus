package org.codingeasy.shiroplus.loader.admin.server.service;

import org.codingeasy.shiroplus.loader.admin.server.models.Log;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.LogsEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.SystemEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.LogsRequest;

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
}
