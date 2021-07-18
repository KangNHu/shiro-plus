package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import java.util.List;

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
}
