package org.codingeasy.shiroplus.core.metadata;

import org.apache.shiro.util.ThreadContext;

import java.util.Map;

/**
* 元数据上下文  
* @author : KangNing Hu
*/
public class MetadataContext {

	private static final String CURRENT_GLOBAL_METADATA_NAME = "_current_global_metadata";
	private static final String CURRENT_PERMISSION_METADATA_NAME = "_current_permission_metadata";


	/**
	 * 设置当前 globalMetadata
	 * @param globalMetadata 全局元信息
	 */
	public static void setCurrentGlobalMetadata(GlobalMetadata globalMetadata){
		Map<Object, Object> resources = ThreadContext.getResources();
		resources.put(CURRENT_GLOBAL_METADATA_NAME , globalMetadata);
	}

	/**
	 * 设置当前 权限元信息
	 * @param permissionMetadata 全局元信息
	 */
	public static void setCurrentPermissionMetadata(PermissionMetadata permissionMetadata){
		Map<Object, Object> resources = ThreadContext.getResources();
		resources.put(CURRENT_PERMISSION_METADATA_NAME , permissionMetadata);
	}


	/**
	 * 获取当前 globalMetadata
	 * @return 返回当前 globalMetadata
	 */
	public static GlobalMetadata getCurrentGlobalMetadata(){
		Map<Object, Object> resources = ThreadContext.getResources();
		return (GlobalMetadata) resources.get(CURRENT_GLOBAL_METADATA_NAME );
	}

	/**
	 * 设置当前 权限元信息
	 * @return 返回当前 permissionMetadata
	 */
	public static PermissionMetadata getCurrentPermissionMetadata(){
		Map<Object, Object> resources = ThreadContext.getResources();
		return (PermissionMetadata) resources.get(CURRENT_PERMISSION_METADATA_NAME);
	}


}
