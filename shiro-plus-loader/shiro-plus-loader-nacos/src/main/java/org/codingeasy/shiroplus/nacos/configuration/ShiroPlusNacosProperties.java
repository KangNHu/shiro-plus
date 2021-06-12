package org.codingeasy.shiroplus.nacos.configuration;

import com.alibaba.nacos.api.config.ConfigType;

/**
* shiro plus配置信息  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class ShiroPlusNacosProperties {

	/**
	 * nacos超时时间 单位毫秒
	 */
	private long timeout = -1;


	/**
	 * 配置数据类型 如json 等等
	 */
	private ConfigType configType = ConfigType.YAML;

	/**
	 * 分组
	 */
	private String group;


	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public ConfigType getConfigType() {
		return configType;
	}

	public void setConfigType(ConfigType configType) {
		this.configType = configType;
	}
}
