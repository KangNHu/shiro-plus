package org.codingeasy.shiro.springboot.config;

import org.codingeasy.shiro.authorize.interceptor.DynamicAuthorizationFilter;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.util.Arrays;

/**
* 动态授权动态配置  
* @author : KangNing Hu
*/
@ConditionalOnBean(ShiroPlusAutoConfiguration.class)
public  class DynamicAuthorizationAutoConfiguration extends AbstractAuthorizationAutoConfiguration {



	@Bean
	public FilterRegistrationBean<DynamicAuthorizationFilter> dynamicAuthorizationFilter(AuthMetadataManager authMetadataManager){
		FilterRegistrationBean<DynamicAuthorizationFilter> filterRegistrationBean = new FilterRegistrationBean<DynamicAuthorizationFilter>();
		filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		filterRegistrationBean.setFilter(createDynamicAuthorizationFilter(authMetadataManager));
		filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
		return filterRegistrationBean;
	}

	/**
	 * 创建动态授权过滤器
	 * @return 返回动态授权过滤器
	 */
	private DynamicAuthorizationFilter createDynamicAuthorizationFilter(AuthMetadataManager authMetadataManager) {
		DynamicAuthorizationFilter dynamicAuthorizationFilter = null;
		if (authExceptionHandler == null){
			dynamicAuthorizationFilter = new DynamicAuthorizationFilter(authMetadataManager);
		}else {
			dynamicAuthorizationFilter = new DynamicAuthorizationFilter(authMetadataManager , this.authExceptionHandler);
		}
		//设置授权处理器
		addAuthorizationHandlers(dynamicAuthorizationFilter);
		return dynamicAuthorizationFilter;
	}



}
