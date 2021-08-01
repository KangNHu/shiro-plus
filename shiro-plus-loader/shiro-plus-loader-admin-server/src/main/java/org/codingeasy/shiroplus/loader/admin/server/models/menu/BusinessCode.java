package org.codingeasy.shiroplus.loader.admin.server.models.menu;

/**
 * 业务码 用于操作日志
 * @author hukangning
 */
public enum BusinessCode {

	//登录
	USER(1),
	//用户
	SYSTEM(2),
	//权限
	PERMISSION(3),
	//全局元信息配置
	GLOBAL_METADATA(4),
	//登录
	LOGIN(5),
	//权限元信息
	PERMISSION_METADATA(6),
	//open api
	OPEN_API(7);

	private int value;

	BusinessCode(int value){
		this.value = value;
	}


	/**
	 * 返回枚举常量
	 * @param businessCode 业务编码
	 * @return 返回后枚举常量值
	 */
	public static int constant(BusinessCode businessCode){
		return businessCode.value;
	}
}
