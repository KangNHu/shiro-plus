package org.codingeasy.shiroplus.loader.admin.server.models.menu;

/**
 * 请求方法类型
 * @author hukangning
 */
public enum RequestMethod {


	GET(org.codingeasy.shiroplus.core.metadata.RequestMethod.GET.name() , 1),
	PUT(org.codingeasy.shiroplus.core.metadata.RequestMethod.PUT.name() , 2),
	DELETE(org.codingeasy.shiroplus.core.metadata.RequestMethod.DELETE.name() , 3),
	POST(org.codingeasy.shiroplus.core.metadata.RequestMethod.POST.name() , 4),
	OPTIONS(org.codingeasy.shiroplus.core.metadata.RequestMethod.OPTIONS.name() , 5),
	PATCH(org.codingeasy.shiroplus.core.metadata.RequestMethod.PATCH.name() , 6),
	HEAD(org.codingeasy.shiroplus.core.metadata.RequestMethod.HEAD.name() , 7);

	private int value;

	private String name;


	/**
	 * 值转换为名称
	 * @param value 值
	 * @return 名称
	 */
	public static String form(int value){
		for (RequestMethod method : RequestMethod.values()){
			if (method.value == value){
				return method.name;
			}
		}
		return null;
	}


	RequestMethod(String name , int value){
		this.name = name;
		this.value = value;
	}


}