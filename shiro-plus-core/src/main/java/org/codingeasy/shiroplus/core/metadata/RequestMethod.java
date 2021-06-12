package org.codingeasy.shiroplus.core.metadata;

/**
 * 请求方法枚举
 * @author kangning <a>2035711178@qq.com</a>
 */
public enum RequestMethod {

	GET("get"),
	PUT("put"),
	DELETE("delete"),
	POST("post"),
	OPTIONS("option"),
	PATCH("patch"),
	HEAD("head");

	/**
	 * 方法名称
	 */
	private String name;


	RequestMethod(String name){
		this.name = name;
	}

	/**
	 * 将字符串转换为枚举
	 * @param method 请求方法名称
	 * @return 返回请求方法名称对应的方法枚举
	 */
	public static RequestMethod form(String method){
		for (RequestMethod requestMethod : RequestMethod.values()){
			if (requestMethod.name.equalsIgnoreCase(method)){
				return requestMethod;
			}
		}
		return null;
	}
}
