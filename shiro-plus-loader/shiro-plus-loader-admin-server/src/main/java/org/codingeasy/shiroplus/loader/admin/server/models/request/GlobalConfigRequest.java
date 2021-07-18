package org.codingeasy.shiroplus.loader.admin.server.models.request;

/**
* 全局配置请求条件  
* @author : KangNing Hu
*/
public class GlobalConfigRequest extends RequestPage {


	/**
	 * 租户id
	 */
	private String tenantId;


	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
