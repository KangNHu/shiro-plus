package org.codingeasy.shiroplus.loader.admin.server.models.menu;

import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.loader.admin.server.models.Dict;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* 授权模式  
* @author : KangNing Hu
*/
public enum  PermissionModel {



	//角色授权模式
	ROLE(1 , PermiModel.ROLE.name()),
	//权限授权模式
	PERMISSION(2 , PermiModel.PERMISSION.name()),
	//票据授权模式
	PRINCIPAL(3 , PermiModel.PRINCIPAL.name()),
	//认证状态授权模式
	AUTHENTICATION(4, PermiModel.AUTHENTICATION.name()),
	//用户信息存在状态的授权模式
	USER(5, PermiModel.USER.name());


	private int value;

	private String name;

	PermissionModel(int value , String name){
		this.value = value;
		this.name = name;
	}

	/**
	 * 转字典
	 * @return
	 */
	public static List<Dict> toDict(){
		return Arrays
				.asList(PermissionModel.values())
				.stream()
				.map(item -> new Dict(item.value , item.name))
				.collect(Collectors.toList());
	}
	/**
	 * 值转换为名称
	 * @param value 值
	 * @return 名称
	 */
	public static String form(int value){
		for (PermissionModel model : PermissionModel.values()){
			if (model.value == value){
				return model.name;
			}
		}
		return null;
	}

}
