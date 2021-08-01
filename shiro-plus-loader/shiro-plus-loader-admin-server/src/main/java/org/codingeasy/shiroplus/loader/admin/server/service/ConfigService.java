package org.codingeasy.shiroplus.loader.admin.server.service;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.server.models.Page;
import org.codingeasy.shiroplus.loader.admin.server.models.dto.AuthMetadataEventWrap;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.GlobalConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.OpenApiEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.entity.PermissionConfigEntity;
import org.codingeasy.shiroplus.loader.admin.server.models.request.GlobalConfigRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.OpenAPiRequest;
import org.codingeasy.shiroplus.loader.admin.server.models.request.PermissionConfigRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ConfigService {

	/**
	 * 获取所有全局配置
	 * @return 返回所有的全局配置信息
	 */
	List<GlobalMetadata> getGlobalMetadataAll();

	/**
	 * 获取所有权限配置
	 * @return 返回所有的权限配置信息
	 */
	List<PermissionMetadata> getPermissionMetadataAll();


	/**
	 * 拉取当前请求待消费的事件
	 * @return 返回所有待消费的事件
	 */
	AuthMetadataEventWrap pullEvents(String instanceCode);

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
	Page<GlobalConfigEntity> globalPage(GlobalConfigRequest request);

	/**
	 * 新增全局元信息
	 * @param globalConfigEntity 待新增的数据
	 * @return
	 */
	int addGlobal(GlobalConfigEntity globalConfigEntity);


	/**
	 * 删除全局元信息
	 * @param id 元信息id
	 * @return
	 */
	int deleteGlobal(Long id);


	/**
	 * 获取全局元信息
	 * @param id 元信息id
	 * @return
	 */
	GlobalConfigEntity getGlobal(Long id);



	/**
	 * 修改全局元信息
	 * @param globalConfigEntity 全局元信息
	 * @return
	 */
	int updateGlobal(GlobalConfigEntity globalConfigEntity);


	/**
	 * 新增权限元信息
	 * @param permissionConfigEntity 待新增的数据
	 * @return
	 */
	int addPermission(PermissionConfigEntity permissionConfigEntity);


	/**
	 * 删除权限元信息
	 * @param id 元信息id
	 * @return
	 */
	int deletePermission(Long id);

	/**
	 * 获取权限元信息
	 * @param id 元信息id
	 * @return
	 */
	PermissionConfigEntity getPermission(Long id);

	/**
	 * 修改权限元信息
	 * @param permissionConfigEntity 权限元信息
	 * @return
	 */
	int updatePermission(PermissionConfigEntity permissionConfigEntity);

	/**
	 * 导入api
	 * <p>这只识别swagger 生成的json 文件 具体格式可以参考 swagger api:v2/api-docs</p>
	 * @param multipartFile swagger json的 文件对象
	 * @return
	 */
	int importApi(MultipartFile multipartFile);


	/**
	 * api接口列表分页
	 * @param request 请对象
	 * @return 返回分页数据
	 */
	Page<OpenApiEntity> apiPage(OpenAPiRequest request);


	/**
	 * 删除api
	 * @param id id
	 * @return
	 */
	int deleteOpenApi(OpenApiEntity id);



	/**
	 * 用于模糊查询列表
	 * @param path  接口路径
	 * @return 返回列表
	 */
	List<OpenApiEntity> likeOpenApi(String path);
}
