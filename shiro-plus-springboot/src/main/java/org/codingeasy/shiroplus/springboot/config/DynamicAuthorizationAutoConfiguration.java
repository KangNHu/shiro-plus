package org.codingeasy.shiroplus.springboot.config;

import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.interceptor.DynamicAuthorizationFilter;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Arrays;

/**
* 动态授权动态配置  
* @author : KangNing Hu
*/
@Configuration
@ConditionalOnBean({AuthMetadataManager.class , EventManager.class})
public  class DynamicAuthorizationAutoConfiguration extends AbstractAuthorizationAutoConfiguration {



	@Bean
	public FilterRegistrationBean<DynamicAuthorizationFilter> dynamicAuthorizationFilter(AuthMetadataManager authMetadataManager,
	                                                                                     EventManager eventManager){
		FilterRegistrationBean<DynamicAuthorizationFilter> filterRegistrationBean = new FilterRegistrationBean<DynamicAuthorizationFilter>();
		filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		filterRegistrationBean.setFilter(createDynamicAuthorizationFilter(authMetadataManager , eventManager));
		filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
		return filterRegistrationBean;
	}

	/**
	 * 创建动态授权过滤器
	 * @return 返回动态授权过滤器
	 */
	private DynamicAuthorizationFilter createDynamicAuthorizationFilter(AuthMetadataManager authMetadataManager,
	                                                                    EventManager eventManager) {
		DynamicAuthorizationFilter dynamicAuthorizationFilter = null;
		if (authExceptionHandler == null){
			dynamicAuthorizationFilter = new DynamicAuthorizationFilter(authMetadataManager ,eventManager);
		}else {
			dynamicAuthorizationFilter = new DynamicAuthorizationFilter(authMetadataManager , this.authExceptionHandler , eventManager);
		}
		//设置授权处理器
		addAuthorizationHandlers(dynamicAuthorizationFilter);
		return dynamicAuthorizationFilter;
	}



}
