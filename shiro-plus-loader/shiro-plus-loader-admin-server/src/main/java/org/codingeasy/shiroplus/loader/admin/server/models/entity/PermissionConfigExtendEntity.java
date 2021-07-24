package org.codingeasy.shiroplus.loader.admin.server.models.entity;

import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.Logical;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.PermissionModel;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


	@Override
	public PermissionMetadata toMetadata() {
		this.setExtend(Optional
				.ofNullable(extendList)
				.orElse(new ArrayList<>())
				.stream()
				.collect(Collectors.toMap(ConfigExtendEntity::getName , ConfigExtendEntity::getValue)));
		return super.toMetadata();
	}
}
