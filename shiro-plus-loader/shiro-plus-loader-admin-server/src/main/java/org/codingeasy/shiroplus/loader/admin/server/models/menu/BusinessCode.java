package org.codingeasy.shiroplus.loader.admin.server.models.menu;

/**
 * 业务码 用于操作日志
 * @author hukangning
 */
public enum BusinessCode {

	USER(1),
	//登录
	LOGIN(1);


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
