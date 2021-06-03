package org.codingeasy.shiro.springboot.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

/**
* shiro plus的filter bean工厂
 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
* @author : KangNing Hu
*/
public class ShiroPlusFilterFactoryBean extends ShiroFilterFactoryBean {


	private FilterChainResolver filterChainResolver;


	public ShiroPlusFilterFactoryBean(FilterChainResolver filterChainResolver){
		this.filterChainResolver = filterChainResolver;
	}

	protected AbstractShiroFilter createInstance() throws Exception {
		AbstractShiroFilter shiroFilter = super.createInstance();
		shiroFilter.setFilterChainResolver(filterChainResolver);
		return shiroFilter;
	}

}
