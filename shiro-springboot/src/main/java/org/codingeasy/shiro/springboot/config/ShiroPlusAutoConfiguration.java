package org.codingeasy.shiro.springboot.config;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.codingeasy.shiro.authorize.metadata.AuthMetadataManager;
import org.codingeasy.shiro.authorize.metadata.MetadataLoader;
import org.codingeasy.shiro.authorize.mgt.DynamicPathMatchingFilterChainResolver;
import org.codingeasy.shiro.authorize.mgt.TenantIdGenerator;
import org.codingeasy.shiro.springboot.AuthMetadataEventPublisher;
import org.codingeasy.shiro.springboot.ShiroFilterClassPathBeanDefinitionScanner;
import org.codingeasy.shiro.springboot.ShiroPlusInitializer;
import org.codingeasy.shiro.springboot.ShiroPlusSecurityManager;
import org.codingeasy.shiro.springboot.annotaion.ShiroFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import java.util.*;

/**
* shiro plus的自动配置  
* @author : KangNing Hu
*/
@Configuration
public class ShiroPlusAutoConfiguration {


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
	 * 注册一个shiro plus的属性对象
	 * @return
	 */
	@ConfigurationProperties("shiro.plus")
	@Bean
	public ShiroPlusProperties shiroPlusProperties(){
		return new ShiroPlusProperties();
	}

	/**
	 * 注册一个权限元信息管理器
	 * @return
	 */
	@Bean
	public AuthMetadataManager authMetadataManager(@Autowired(required = false) MetadataLoader metadataLoader){
		return new AuthMetadataManager(metadataLoader);
	}


	@Bean
	@ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
	public ShiroFilterFactoryBean  shiroFilterFactoryBean(SecurityManager securityManager ,
	                                                      AuthMetadataManager authMetadataManager,
	                                                      ShiroPlusProperties shiroPlusProperties,
	                                                      ApplicationContext applicationContext,
	                                                      @Autowired(required = false) TenantIdGenerator tenantIdGenerator){

		//创建shiro filter bean
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroPlusFilterFactoryBean(createFilterChainResolve(authMetadataManager , tenantIdGenerator));
		//处理过滤链定义 如配置为 anon -> url 要转为 url -> anon
		Map<String, String> filterChainDefinition = shiroPlusProperties.getFilterChainDefinition();
		if (!MapUtils.isEmpty(filterChainDefinition)){
			Map<String , String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
			for (Map.Entry<String ,String> entry : filterChainDefinition.entrySet()){
				filterChainDefinitionMap.put(entry.getValue() , entry.getKey());
			}
			shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		}
		shiroFilterFactoryBean.setFilters(getFilters(applicationContext));
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
	 * 创建过滤链解析器
	 * @return  返回过滤链解析器
	 */
	private DynamicPathMatchingFilterChainResolver createFilterChainResolve(AuthMetadataManager authMetadataManager , TenantIdGenerator tenantIdGenerator){
		//创建过滤链解析器
		DynamicPathMatchingFilterChainResolver filterChainResolver = new DynamicPathMatchingFilterChainResolver(authMetadataManager);
		if (tenantIdGenerator != null){
			filterChainResolver.setTenantIdGenerator(tenantIdGenerator);
		}
		return filterChainResolver;
	}


	/**
	 * 获取shiro 的filters
	 * <p>shiro 的filter不能被spring 进行管理防止重复注册</p>
	 * @return 返回 filter名称 -> filter 的一个map
	 */
	private Map<String , Filter> getFilters(ApplicationContext applicationContext){
		//创建 shiro fiter扫描器
		ShiroFilterClassPathBeanDefinitionScanner scanner = new ShiroFilterClassPathBeanDefinitionScanner((AbstractAutowireCapableBeanFactory) applicationContext.getAutowireCapableBeanFactory());
		//获取spring boot的包路径
		List<String> packages = AutoConfigurationPackages.get(applicationContext);
		return scanner.getFiltersByPackage(packages);
	}


}
