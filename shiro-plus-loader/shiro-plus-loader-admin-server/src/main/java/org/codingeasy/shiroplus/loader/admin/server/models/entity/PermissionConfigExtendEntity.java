package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import java.util.List;

/**
* 权限配置和扩展  
* @author : KangNing Hu
*/
public class PermissionConfigExtendEntity extends PermissionConfigEntity{

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
