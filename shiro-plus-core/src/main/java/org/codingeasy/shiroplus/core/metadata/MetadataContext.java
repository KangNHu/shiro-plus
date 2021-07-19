package org.codingeasy.shiroplus.core.metadata;


import java.util.HashMap;
import java.util.Map;

/**
* 元数据上下文
 * <p>在web场景下由于主流的web容器都是使用线程复用技术，则不需要释放资源</p>
 * <p>如果实在非池化线程中使用则需要手动释放 {@link this#remove()}</p>
* @author : KangNing Hu
*/
public class MetadataContext {

	private static final String CURRENT_GLOBAL_METADATA_NAME = "_current_global_metadata";
	private static final String CURRENT_PERMISSION_METADATA_NAME = "_current_permission_metadata";


	private final static ThreadLocal<Map<String ,Metadata>> CURRENT_METADATA = new ThreadLocal<>();

	/**
	 * 设置当前 globalMetadata
	 * @param globalMetadata 全局元信息
	 */
	public static void setCurrentGlobalMetadata(GlobalMetadata globalMetadata){
		Map<String, Metadata> resources = getResource();
		if (globalMetadata != null) {
			resources.put(CURRENT_GLOBAL_METADATA_NAME, globalMetadata);
		}
	}

	/**
	 * 设置当前 权限元信息
	 * @param permissionMetadata 全局元信息
	 */
	public static void setCurrentPermissionMetadata(PermissionMetadata permissionMetadata){
		Map<String, Metadata> resources = getResource();
		if (permissionMetadata != null) {
			resources.put(CURRENT_PERMISSION_METADATA_NAME, permissionMetadata);
		}
	}


	/**
	 * 获取当前 globalMetadata
	 * @return 返回当前 globalMetadata
	 */
	public static GlobalMetadata getCurrentGlobalMetadata(){
		Map<String, Metadata> resources = getResource();
		return (GlobalMetadata) resources.get(CURRENT_GLOBAL_METADATA_NAME );
	}

	/**
	 * 设置当前 权限元信息
	 * @return 返回当前 permissionMetadata
	 */
	public static PermissionMetadata getCurrentPermissionMetadata(){
		Map<String, Metadata> resources = getResource();
		return (PermissionMetadata) resources.get(CURRENT_PERMISSION_METADATA_NAME);
	}

	/**
	 * 移除数据源
	 */
	public static void remove(){
		CURRENT_METADATA.remove();
	}

	/**
	 * 返回数据源
	 * @return 返回数据源， 如果当前线程没有则创建，有则返回
	 */
	 private static Map<String , Metadata> getResource(){
		Map<String, Metadata> resource = CURRENT_METADATA.get();
		if (resource == null){
			resource = new HashMap<>();
		}
		CURRENT_METADATA.set(resource);
		return resource;
	}
}
