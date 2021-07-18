package org.codingeasy.shiroplus.loader.admin.server.models.menu;

/**
* 通用状态  
* @author : KangNing Hu
*/
public enum  CommonStatus {

	/**
	 * 删除
	 */
	DELETE(0),
	/**
	 * 正常
	 */
	NORMAL(1),
	/**
	 *禁用
	 */
	DISABLE(2);



	private int value;

	CommonStatus(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * 返回枚举常量
	 * @param status 状态
	 * @return 返回后枚举常量值
	 */
	public static int constant(CommonStatus status){
		return status.value;
	}
}
