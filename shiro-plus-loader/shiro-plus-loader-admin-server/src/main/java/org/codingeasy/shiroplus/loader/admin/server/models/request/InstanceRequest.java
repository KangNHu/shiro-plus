package org.codingeasy.shiroplus.loader.admin.server.models.request;

/**
* 实例的查询条件  
* @author : KangNing Hu
*/
public class InstanceRequest extends RequestPage{

	/**
	 * 服务名称
	 */
	private String name;

	/**
	 * ip
	 */
	private String ip;


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
