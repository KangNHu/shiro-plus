package org.codingeasy.shiro.springboot.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
* shiro plus的配置文件  
* @author : KangNing Hu
*/
public class ShiroPlusProperties {

	/**
	 * 过滤链定义
	 * 如 anon -> /**
	 * {@link ShiroFilterFactoryBean#setFilterChainDefinitionMap(Map)}
	 */
	private LinkedHashMap<String , String> filterChainDefinition = new LinkedHashMap<String, String>();


	/**
	 * 过滤链定义配置路径
	 * {@link ShiroFilterFactoryBean#setFilterChainDefinitions(String)}
	 */
	private String definitions;


	/**
	 * 登录地址
	 * {@link ShiroFilterFactoryBean#setLoginUrl(String)}
	 */
	private String loginUrl;


	/**
	 * 登录成功url
	 * {@link ShiroFilterFactoryBean#setSuccessUrl(String)}
	 */
	private String successUrl;


	/**
	 * 未授权url
	 * {@link ShiroFilterFactoryBean#setUnauthorizedUrl(String)}
	 */
	private String unauthorizedUrl;


	public Map<String, String> getFilterChainDefinition() {
		return filterChainDefinition;
	}

	public void setFilterChainDefinition(LinkedHashMap<String, String> filterChainDefinition) {
		this.filterChainDefinition = filterChainDefinition;
	}

	public String getDefinitions() {
		return definitions;
	}

	public void setDefinitions(String definitions) {
		this.definitions = definitions;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
}
