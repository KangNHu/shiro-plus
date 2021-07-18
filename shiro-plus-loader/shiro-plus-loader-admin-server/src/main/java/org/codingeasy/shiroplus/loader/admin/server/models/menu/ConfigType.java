package org.codingeasy.shiroplus.loader.admin.server.models.menu;

/**
 * 配置类型
 * @author hukangning
 */
public enum ConfigType {

	PERMISSION(1),

	GLOBAL(2);

	private int value;


	ConfigType(int value){
		this.value = value;
	}


	/**
	 * 返回枚举常量
	 * @param type 类型
	 * @return 返回后枚举常量值
	 */
	public static int constant(ConfigType type){
		return type.value;
	}
}
