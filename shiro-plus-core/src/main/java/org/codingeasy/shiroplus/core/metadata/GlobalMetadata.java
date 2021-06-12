package org.codingeasy.shiroplus.core.metadata;

import org.codingeasy.shiroplus.core.utils.CopyUtils;

import java.util.ArrayList;
import java.util.List;

/**
* 全局元信息  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class GlobalMetadata extends AbstractMetadata{


	public GlobalMetadata(String tenantId, List<String> anons, Boolean enableAuthentication, Boolean enableAuthorization) {
		this.tenantId = tenantId;
		this.anons = anons;
		this.enableAuthentication = enableAuthentication;
		this.enableAuthorization = enableAuthorization;
	}

	public GlobalMetadata() {
	}

	/**
	 * 租户id
	 * <p>如果不需要多租户则可以不用处理</p>
	 */
	private String tenantId;

	/**
	 * 不需要拦截的请求
	 */
	private List<String> anons;



	/**
	 * 是否开启鉴权
	 * <p>如果为空或者true则开始否则关闭</p>
	 */
	private Boolean enableAuthentication;


	/**
	 * 是否开启授权
	 * <p>如果为空或者true则开始否则关闭</p>
	 */
	private Boolean enableAuthorization;


	@Override
	protected GlobalMetadata clone() throws CloneNotSupportedException {
		GlobalMetadata clone = (GlobalMetadata)super.clone();
		clone.setAnons(CopyUtils.copyList(this.anons));
		return clone;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public List<String> getAnons() {
		return anons;
	}

	public void setAnons(List<String> anons) {
		this.anons = anons;
	}

	public Boolean getEnableAuthentication() {
		return enableAuthentication;
	}

	public void setEnableAuthentication(Boolean enableAuthentication) {
		this.enableAuthentication = enableAuthentication;
	}

	public Boolean getEnableAuthorization() {
		return enableAuthorization;
	}

	public void setEnableAuthorization(Boolean enableAuthorization) {
		this.enableAuthorization = enableAuthorization;
	}

	@Override
	public String toString() {
		return "GlobalMetadata{" +
				"tenantId='" + tenantId + '\'' +
				", anons=" + anons +
				", enableAuthentication=" + enableAuthentication +
				", enableAuthorization=" + enableAuthorization +
				'}';
	}
}
