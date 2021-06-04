package org.codingeasy.shiro.springboot.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.codingeasy.shiro.authorize.interceptor.DynamicAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.Ordered;

/**
* shiro plus的filter bean工厂
 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
* @author : KangNing Hu
*/
public class ShiroPlusFilterFactoryBean extends ShiroFilterFactoryBean   {


	private final Logger logger = LoggerFactory.getLogger(ShiroPlusFilterFactoryBean.class);

	private PathMatchingFilterChainResolver filterChainResolver;


	public ShiroPlusFilterFactoryBean(PathMatchingFilterChainResolver filterChainResolver){
		this.filterChainResolver = filterChainResolver;
	}

	protected AbstractShiroFilter createInstance() throws Exception {
		logger.debug("Creating Shiro Filter instance.");
		SecurityManager securityManager = this.getSecurityManager();
		String msg;
		if (securityManager == null) {
			msg = "SecurityManager property must be set.";
			throw new BeanInitializationException(msg);
		} else if (!(securityManager instanceof WebSecurityManager)) {
			msg = "The security manager does not implement the WebSecurityManager interface.";
			throw new BeanInitializationException(msg);
		} else {
			FilterChainManager manager = this.createFilterChainManager();
			filterChainResolver.setFilterChainManager(manager);
			return new SpringShiroPlusFilter((WebSecurityManager)securityManager, filterChainResolver);
		}
	}

	/**
	 * shiro plush的核心过滤器
	 * 覆盖 shiro默认的filter 保证 {@link DynamicAuthorizationFilter} 最后执行
	 */
	private static final class SpringShiroPlusFilter extends AbstractShiroFilter implements Ordered{
		protected SpringShiroPlusFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
			if (webSecurityManager == null) {
				throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
			} else {
				this.setSecurityManager(webSecurityManager);
				if (resolver != null) {
					this.setFilterChainResolver(resolver);
				}

			}
		}

		public int getOrder() {
			return Ordered.LOWEST_PRECEDENCE -1;
		}
	}

}
