package org.codingeasy.shiroplus.loader.admin.server.models.request;

/**
* 查询对象  
* @author : KangNing Hu
*/
public class OpenAPiRequest extends RequestPage{

	/**
	 * path
	 */
	private String path;


	/**
	 * 请求方式
	 */
	private Integer method;


	public Integer getMethod() {
		return method;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
