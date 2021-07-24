package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* 全局配置和扩展  
* @author : KangNing Hu
*/
public class GlobalConfigExtendEntity extends GlobalConfigEntity {


	/**
	 * 扩展列表
	 */
	private List<ConfigExtendEntity> extendList;


	public List<ConfigExtendEntity> getExtendList() {
		return extendList;
	}

	public void setExtendList(List<ConfigExtendEntity> extendList) {
		this.extendList = extendList;
	}


	@Override
	public GlobalMetadata toMetadata() {
		this.setExtend(Optional
				.ofNullable(extendList)
				.orElse(new ArrayList<>())
				.stream()
				.collect(Collectors.toMap(ConfigExtendEntity::getName , ConfigExtendEntity::getValue)));
		return super.toMetadata();
	}
}
