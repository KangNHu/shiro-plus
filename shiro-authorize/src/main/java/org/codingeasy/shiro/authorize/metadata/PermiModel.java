package org.codingeasy.shiro.authorize.metadata;

/**
 * 权限模式
 * @author hukangning
 */
public enum PermiModel {


	//角色授权模式
	ROLE("role"),
	//权限授权模式
	PERMISSION("permission"),
	//票据授权模式
	PRINCIPAL("principal"),
	//认证状态授权模式
	AUTHENTICATION("authentication"),
	//用户信息存在状态的授权模式
	USER("user");

	private String name;

	PermiModel(String name){
		this.name = name;
	}


	/**
	 * 将模式名称转换为模式枚举
	 * @param model 模式名称
	 * @return 返回一个模式名称对应的模式枚举
	 */
	public static PermiModel form(String model){
		for (PermiModel permiModel :PermiModel.values() ){
			if (permiModel.name.equalsIgnoreCase(model)){
				return permiModel;
			}
		}
		return null;
	}

}
