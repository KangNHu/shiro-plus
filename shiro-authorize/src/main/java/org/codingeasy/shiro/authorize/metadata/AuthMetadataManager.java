package org.codingeasy.shiro.authorize.metadata;

import org.apache.shiro.event.Subscribe;
import org.apache.shiro.event.support.EventListener;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Initializable;
import org.codingeasy.shiro.authorize.ReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
* 权限元信息管理器  
* @author : KangNing Hu
*/
public class AuthMetadataManager implements EventListener , Initializable {

	public static final String DEFAULT_TENANT_ID = "default_tenant_Id";

	private final Logger logger = LoggerFactory.getLogger(AuthMetadataManager.class);

	private Map<String , Map< PermiModel, PermissionMetadata>> permissionMetadataMap = new HashMap<>();

	private Map<String , GlobalMetadata> globalMetadataMap = new HashMap<>();

	private ReadWriteLock permissionMetadataLock = new ReadWriteLock();

	private ReadWriteLock globalMetadataLock = new ReadWriteLock();


	private MetadataLoader metadataLoader;


	public AuthMetadataManager(MetadataLoader metadataLoader){
		this.metadataLoader = metadataLoader;
	}


	/**
	 * 初始化管理器
	 */
	public void init(){
		if (metadataLoader != null){
			//初始化权限元信息
			List<PermissionMetadata> permissionMetadataList = metadataLoader.load();
			if (!CollectionUtils.isEmpty(permissionMetadataList)) {
				for (PermissionMetadata permissionMetadata : permissionMetadataList) {
					logger.info("加载权限元信息 {}", permissionMetadata.toString());
					String cacheKey = getCacheKey(permissionMetadata);
					Map<PermiModel, PermissionMetadata> map = permissionMetadataMap.computeIfAbsent(cacheKey, key -> new HashMap<>());
					map.put(permissionMetadata.getPermiModel() , permissionMetadata);
				}
			}
			//初始化全局元信息
			List<GlobalMetadata> globalMetadataList = metadataLoader.loadGlobal();
			if (!CollectionUtils.isEmpty(globalMetadataList)) {
				for (GlobalMetadata globalMetadata : globalMetadataList){
					logger.info("加载权限全局元信息 {}", globalMetadata.toString());
					String tenantId = globalMetadata.getTenantId();
					globalMetadataMap.put(tenantId == null ? DEFAULT_TENANT_ID : tenantId , globalMetadata);
				}
			}
		}
	}


	/**
	 * 获取权限元数据
	 * @param key 权限元数据key
	 * @param permiModel 授权模型
	 * @return  返回权限元数据，如果没有则返回null ,且返回的是一份克隆数据
	 */
	public PermissionMetadata getPermissionMetadata(String key  ,PermiModel permiModel){
		try {
			permissionMetadataLock.readLock();
			Map<PermiModel, PermissionMetadata> map = permissionMetadataMap.get(key);
			if (map == null){
				return null;
			}
			PermissionMetadata permissionMetadata = map.get(permiModel);
			return permissionMetadata == null ? null : permissionMetadata.clone();
		}catch (CloneNotSupportedException e) {
			logger.warn("clone error" ,e);
			return null;
		} finally {
			permissionMetadataLock.unReadLock();
		}
	}




	/**
	 * 获取全局元信息
	 * @param tenantId 租户id
	 * @return 返回全局元信息
	 */
	public GlobalMetadata getGlobalMetadata(String tenantId){
		try {
			globalMetadataLock.readLock();
			GlobalMetadata globalMetadata = globalMetadataMap.get(tenantId);
			return globalMetadata == null ? null : globalMetadata.clone();
		} catch (CloneNotSupportedException e) {
			logger.warn("clone error" ,e);
			return null;
		} finally {
			globalMetadataLock.unReadLock();
		}
	}



	/**
	 * 获取元信息缓存key
	 * @param permissionMetadata 权限元信息
	 * @return 返回缓存key
	 */
	private String getCacheKey(PermissionMetadata permissionMetadata) {
		return permissionMetadata.getPath() + ":" + permissionMetadata.getMethod();
	}



	public boolean accepts(Object o) {
		return o instanceof AuthMetadataEvent;
	}


	@Subscribe
	public void onEvent(Object o) {
		AuthMetadataEvent authMetadataEvent = (AuthMetadataEvent) o;
		//处理权限元信息事件
		handPermissionMetadataEvent(authMetadataEvent);
		//处理全局元信息
		handGlobalMetadataEvent(authMetadataEvent);
	}

	/**
	 * 处理全局元信息事件
	 * @param event 事件对象
	 */
	private void handGlobalMetadataEvent(AuthMetadataEvent event) {
		Object metadata = event.getMetadata();
		if (!(metadata instanceof GlobalMetadata)){
			return;
		}
		GlobalMetadata globalMetadata = (GlobalMetadata) metadata;
		String tenantId = globalMetadata.getTenantId();
		doHandMetadataEvent(
				event ,
				tenantId ,
				globalMetadataLock ,
				globalMetadataMap ,
				() -> globalMetadataMap.put(tenantId == null ? DEFAULT_TENANT_ID : tenantId , globalMetadata)
		);
	}


	/**
	 * 处理权限元信息事件
	 * @param event 事件对象
	 */
	private void handPermissionMetadataEvent(AuthMetadataEvent event){
		Object metadata = event.getMetadata();
		if (!(metadata instanceof PermissionMetadata)){
			return;
		}
		PermissionMetadata permissionMetadata = (PermissionMetadata) metadata;
		String cacheKey = getCacheKey(permissionMetadata);
		doHandMetadataEvent(
				event ,
				cacheKey ,
				permissionMetadataLock ,
				permissionMetadataMap,
				() ->{
					Map<PermiModel, PermissionMetadata> map = permissionMetadataMap.computeIfAbsent(cacheKey, key -> new HashMap<>());
					map.put(permissionMetadata.getPermiModel() , permissionMetadata);
				}
		);
	}


	/**
	 *
	 * @param event 事件对象
	 * @param cacheKey 元数据缓存key
	 * @param readWriteLock 元数据缓存所属的读锁
	 * @param metadataMap 元数据缓存对象
	 */
	private void doHandMetadataEvent(AuthMetadataEvent event ,
	                                 String cacheKey ,
	                                 ReadWriteLock readWriteLock ,
	                                 Map metadataMap , Callback callback){
		EventType type = event.getType();
		if (type == null){
			logger.info("无效的 metadata event");
			return;
		}
		Object metadata = event.getMetadata();
		try {
			readWriteLock.writeLock();
			switch (type){
				case DELETE://删除
					metadataMap.remove(cacheKey);
					break;
				case UPDATE://更新
				case ADD://新增
					callback.call();
					break;
				case CLEAN://清除
					metadataMap.clear();
			}
			logger.info("处理 metadata event {}" , metadata.toString());
		}finally {
			readWriteLock.unWriteLock();
		}
	}


	/**
	 * 回调接口
	 */
	@FunctionalInterface
	interface Callback{
		void call();
	}
}
