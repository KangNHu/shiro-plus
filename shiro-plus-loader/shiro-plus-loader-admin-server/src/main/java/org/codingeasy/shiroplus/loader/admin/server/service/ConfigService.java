package org.codingeasy.shiroplus.loader.admin.server.service;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.GlobalConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionConfigRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ConfigService {

	/**
	 * 获取所有全局配置
	 * @return 返回所有的全局配置信息
	 */
	List<GlobalConfigEntity> getGlobalConfigs();

	/**
	 * 获取所有权限配置
	 * @return 返回所有的权限配置信息
	 */
	List<PermissionConfigEntity> getPermissionConfigs();


	/**
	 * 拉取当前请求待消费的事件
	 * @return 返回所有待消费的事件
	 */
	List<AuthMetadataEvent> pullEvents(HttpServletRequest request);

	/**
	 * 权限元数据条件分页列表
	 * @param request 请求条件
	 * @return 返回符合条件的权限配置列表
	 */
	Page<PermissionConfigEntity> permissionPage(PermissionConfigRequest request);

	/**
	 * 全局元数据条件分页列表
	 * @param request
	 * @return
	 */
	Page<GlobalConfig> globalPage(GlobalConfigRequest request);
}
