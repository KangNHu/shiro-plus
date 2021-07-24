package org.codingeasy.shiroplus.core.metadata;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.event.support.EventListener;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Initializable;
import org.codingeasy.shiroplus.core.ReadWriteLock;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
* 权限元信息管理器  
* @author :kangning <a>2035711178@qq.com</a>
*/
public class AuthMetadataManager implements EventListener , Initializable {

	public static final String DEFAULT_TENANT_ID = "default_tenant_Id";

	private final Logger logger = LoggerFactory.getLogger(AuthMetadataManager.class);

	private Map<String , PermissionMetadata> permissionMetadataMap = new HashMap<>();

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
					//如果key存在空则忽略
					if (permissionMetadata.getMethod() == null ||
							StringUtils.isEmpty(permissionMetadata.getPath() )){
						logger.info("配置的主要信息为空，忽略配置 {}" , permissionMetadata.toString());
						continue;
					}
					logger.info("加载权限元信息 {}", permissionMetadata.toString());
					permissionMetadataMap.put(getCacheKey(permissionMetadata), permissionMetadata);
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
			PermissionMetadata permissionMetadata = permissionMetadataMap.get(key);
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
		doHandMetadataEvent(
				event,
				gm -> gm.getTenantId() == null ? DEFAULT_TENANT_ID : gm.getTenantId()  ,
				globalMetadataLock ,
				globalMetadataMap ,
				(gm, cacheKey) -> globalMetadataMap.put(cacheKey , gm),
				GlobalMetadata.class
		);
	}


	/**
	 * 处理权限元信息事件
	 * @param event 事件对象
	 */
	private void handPermissionMetadataEvent(AuthMetadataEvent event){
		doHandMetadataEvent(
				event ,
				this::getCacheKey ,
				permissionMetadataLock ,
				permissionMetadataMap,
				(pm , cacheKey) -> permissionMetadataMap.put(cacheKey ,pm ),
				PermissionMetadata.class
		);
	}


	/**
	 *
	 * @param event 事件对象
	 * @param cacheKey 元数据缓存key回调
	 * @param readWriteLock 元数据缓存所属的读锁
	 * @param metadataMap 元数据缓存对象
	 * @param clazz 事件源元素类型
	 */
	private <T>void doHandMetadataEvent(AuthMetadataEvent event ,
	                                 Function<T , String> cacheKey,
	                                 ReadWriteLock readWriteLock ,
	                                 Map metadataMap ,
                                     BiConsumer<T ,String> callback,
	                                 Class<T> clazz){
		EventType type = event.getType();
		if (type == null){
			logger.info("无效的 metadata event");
			return;
		}
		//处理list 或 单个元素
		Object metadata = event.getSource();
		List<Object> list = new ArrayList<>();
		if (metadata instanceof List){
			list = (List<Object>) metadata;
		}else {
			list.add(metadata);
		}
		//进行变更处理
		try {
			readWriteLock.writeLock();
			for (Object o : list) {
				if (!clazz.isAssignableFrom(o.getClass())){
					if (logger.isDebugEnabled()){
						logger.debug("事件源类型不匹配 {}" , o.getClass());
					}
					continue;
				}
				String key = cacheKey.apply((T) o);
				switch (type) {
					case DELETE://删除
						metadataMap.remove(key);
						break;
					case UPDATE://更新
					case ADD://新增
						callback.accept((T) o , key);
						break;
					case CLEAN://清除
						metadataMap.clear();
				}
				logger.info("处理 metadata event {} 事件类型 {} " ,
						metadata.toString() , type.name());
			}
		}finally {
			readWriteLock.unWriteLock();
		}
	}


	public static void main(String[] args) {
		System.out.println("sss" + null);
	}
}
