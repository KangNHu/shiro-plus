package org.codingeasy.shiroplus.loader.admin.server.models.menu;

/**
 * 逻辑
 * @author hukangning
 */
public enum Logical{


	//角色授权模式
	AND( 1, org.codingeasy.shiroplus.core.metadata.Logical.AND.name()),
	//权限授权模式
	OR(2 , org.codingeasy.shiroplus.core.metadata.Logical.OR.name());


	private int value;

	private String name;


	Logical(int value , String name){
		this.value = value;
		this.name = name;
	}

	/**
	 * 值转换为名称
	 * @param value 值
	 * @return 名称
	 */
	public static String form(int value){
		for (Logical logical : Logical.values()){
			if (logical.value == value){
				return logical.name;
			}
		}
		return null;
	}
}
