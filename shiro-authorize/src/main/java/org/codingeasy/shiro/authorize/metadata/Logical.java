package org.codingeasy.shiro.authorize.metadata;

/**
 *
 * 授权逻辑 如有多个权限进行校验时 是 and 还是 or
 * @author hukangning
 */
public enum Logical {

	//角色授权模式
	AND("and"),
	//权限授权模式
	OR("or");

	private String name;

	Logical(String name){
		this.name = name;
	}


	/**
	 * 名称转换为模式枚举
	 * @param name 逻辑名称
	 * @return 返回一个逻辑名称对应的模式枚举
	 */
	public static Logical form(String name){
		for (Logical logical :Logical.values() ){
			if (logical.name.equalsIgnoreCase(name)){
				return logical;
			}
		}
		return null;
	}


	/**
	 * 将shiro{@link org.apache.shiro.authz.annotation.Logical}转换为模式枚举
	 * @param logical shiro逻辑枚举
	 * @return 返回一个shiro逻辑枚举对应的模式枚举
	 */
	public static Logical form(org.apache.shiro.authz.annotation.Logical logical){
		return logical == org.apache.shiro.authz.annotation.Logical.AND ? Logical.AND : Logical.OR;
	}
}
