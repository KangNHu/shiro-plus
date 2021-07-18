package org.codingeasy.shiroplus.loader.admin.client.configuration;

/**
* 管理服务的配置  
* @author : KangNing Hu
*/
public class AdminServerProperties {

	/**
	 * admin server 的host 如 http://localhost:8082/shiroplus
	 */
	private String host;

	/**
	 * 访问的token
	 */
	private String token;

	/**
	 * 刷新间隔
	 */
	private long refreshInterval;


	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(long refreshInterval) {
		if (refreshInterval == 0){
			return;
		}
		this.refreshInterval = refreshInterval;
	}
}
