package org.codingeasy.shiroplus.core.metadata;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;

import java.util.List;

/**
* 元数据加载器
 * <p>
 *     对已加载的数据会做一级缓存处理，如:元数据发生变更可以通过发送{@link AuthMetadataEvent}事件
 *     来触发缓存的更新
 * </p>
 * <code>
 *
 * </code>
 *
* @author : kangning <a>2035711178@qq.com</a>
*/
public interface MetadataLoader {

	/**
	 * 加载元数据列表
	 * @return 返回所有的元数据列表
	 */
	List<PermissionMetadata> load();

	/**
	 * 加载全局元信息
	 * <p>如果不需要多租户的支持tenantId可以不用考虑</p>
	 * @return 返回全局元信息
	 */
	List<GlobalMetadata> loadGlobal();
}
