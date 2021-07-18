package org.codingeasy.shiroplus.loader.admin.server.models.menu;

/**
 * 操作类型
 * @author hukangning
 */
public enum OperationType {


	ADD(1),UPDATE(2),DELETE(3);


	private int value;

	OperationType(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * 返回枚举常量
	 * @param operationType 操作类型
	 * @return 返回后枚举常量值
	 */
	public static int constant(OperationType operationType){
		return operationType.value;
	}

}
