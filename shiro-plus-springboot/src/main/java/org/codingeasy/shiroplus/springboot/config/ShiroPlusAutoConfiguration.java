package org.codingeasy.shiroplus.springboot.config;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.Assert;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.AuthMetadataManager;
import org.codingeasy.shiroplus.core.realm.HttpServletAuthFilter;
import org.codingeasy.shiroplus.core.realm.processor.AuthProcessor;
import org.codingeasy.shiroplus.core.realm.HttpServletAuthRealm;
import org.codingeasy.shiroplus.core.realm.AuthenticationExceptionStrategy;
import org.codingeasy.shiroplus.springboot.ShiroFilterClassPathBeanDefinitionScanner;
import org.codingeasy.shiroplus.springboot.ShiroPlusSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import sun.net.httpserver.AuthFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.codingeasy.shiroplus.core.realm.HttpServletAuthFilter.AUTH_FILTER_NAME;

/**
* shiroplus plus的自动配置
* @author : kangning <a>2035711178@qq.com</a>
*/
public class ShiroPlusAutoConfiguration extends AbstractAuthorizationAutoConfiguration<HttpServletRequest , HttpServletResponse>{

	public static final String DEFAULT_FILTERS_BEAN_NAME = "_defaultFilters";
	/**
	 * 注册shiro plus的安全管理器到spring
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SecurityManager.class)
	public SecurityManager securityManager(){
		return new ShiroPlusSecurityManager();
	}


	/**
	 * 注册通用的realm
	 * @param authProcessor
	 * @return
	 */
	@Bean
	public HttpServletAuthRealm authServletRealm(AuthProcessor<HttpServletRequest , HttpServletResponse> authProcessor){
		return new HttpServletAuthRealm(authProcessor);
	}

	/**
	 * 注册默认的filter
	 * @param authMetadataManager
	 * @param eventManager
	 * @param authProcessor
	 * @return
	 */
	@Bean(name = DEFAULT_FILTERS_BEAN_NAME)
	public Map<String , Filter> defaultFilters(AuthMetadataManager authMetadataManager,
	                                           EventManager eventManager,
	                                           @Autowired(required = false) AuthProcessor<HttpServletRequest , HttpServletResponse> authProcessor){
		Map<String , Filter> defaultFilters = new LinkedHashMap<>();
		Assert.notNull(authProcessor , "authProcessor is not null");
		HttpServletAuthFilter httpServletAuthFilter = new HttpServletAuthFilter(authMetadataManager, eventManager, authProcessor);
		setCommonComponent(httpServletAuthFilter);
		defaultFilters.put(AUTH_FILTER_NAME , httpServletAuthFilter);
		return defaultFilters;
	}
	/**
	 * 鉴权策略
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(AuthenticationStrategy.class)
	public AuthenticationStrategy authenticationStrategy(){
		return new AuthenticationExceptionStrategy();
	}

	/**
	 * 注册一个shiro plus的属性对象
	 * @return
	 */
	@Bean
	@ConfigurationProperties("shiroplus.plus")
	public ShiroPlusProperties shiroPlusProperties(){
		return new ShiroPlusProperties();
	}



	@Bean
	@ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
	public ShiroFilterFactoryBean  shiroFilterFactoryBean(SecurityManager securityManager ,
	                                                      AuthMetadataManager authMetadataManager,
	                                                      ShiroPlusProperties shiroPlusProperties,
	                                                      ApplicationContext applicationContext,
	                                                      @Autowired @Qualifier(DEFAULT_FILTERS_BEAN_NAME) Map<String , Filter> defaultFilters){
		//创建shiro filter bean
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//处理过滤链定义 如配置为 anon -> url 要转为 url -> anon
		Map<String, String> filterChainDefinition = shiroPlusProperties.getFilterChainDefinition();
		if (!MapUtils.isEmpty(filterChainDefinition)){
			Map<String , String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
			for (Map.Entry<String ,String> entry : filterChainDefinition.entrySet()){
				filterChainDefinitionMap.put(entry.getValue() , entry.getKey());
			}
			shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		}
		shiroFilterFactoryBean.setFilters(getFilters(defaultFilters , applicationContext));
		//设置基本配置
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl(shiroPlusProperties.getLoginUrl());
		shiroFilterFactoryBean.setSuccessUrl(shiroPlusProperties.getSuccessUrl());
		shiroFilterFactoryBean.setUnauthorizedUrl(shiroPlusProperties.getUnauthorizedUrl());
		//处理过滤链定义配置加载
		String definitions = shiroPlusProperties.getDefinitions();
		if (definitions != null) {
			shiroFilterFactoryBean.setFilterChainDefinitions(definitions);
		}
		return shiroFilterFactoryBean;
	}




	/**
	 * 获取shiro 的filters
	 * <p>shiroplus 的filter不能被spring 进行管理防止重复注册</p>
	 * @return 返回 filter名称 -> filter 的一个map
	 */
	private Map<String , Filter> getFilters(Map<String , Filter> defaultFilters ,ApplicationContext applicationContext){
		//创建 shiroplus fiter扫描器
		ShiroFilterClassPathBeanDefinitionScanner scanner = new ShiroFilterClassPathBeanDefinitionScanner((AbstractAutowireCapableBeanFactory) applicationContext.getAutowireCapableBeanFactory());
		//获取spring boot的包路径
		List<String> packages = AutoConfigurationPackages.get(applicationContext);
		defaultFilters.putAll(scanner.getFiltersByPackage(packages));
		return defaultFilters;
	}


}
