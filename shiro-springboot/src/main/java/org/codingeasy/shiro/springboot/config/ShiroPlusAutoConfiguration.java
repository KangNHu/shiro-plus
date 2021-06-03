package org.codingeasy.shiro.springboot.config;

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
import org.codingeasy.shiro.springboot.ShiroPlusInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.*;

/**
* shiro plus的自动配置  
* @author : KangNing Hu
*/
public class ShiroPlusAutoConfiguration {



	private List<Realm> realms = new ArrayList<Realm>();

	private CacheManager cacheManager;

	private RememberMeManager rememberMeManager;

	private SessionManager sessionManager;

	private SubjectFactory subjectFactory;

	private Authenticator authenticator;

	private Authorizer authorizer;

	private EventBus eventBus;

	private MetadataLoader metadataLoader;

	private TenantIdGenerator tenantIdGenerator;


	/**
	 * 注册shiro bean的生命周期处理器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(LifecycleBeanPostProcessor.class)
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 注册shiro的安全管理器到spring
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SecurityManager.class)
	public SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealms(realms);
		if (cacheManager != null) {
			securityManager.setCacheManager(cacheManager);
		}
		if (rememberMeManager != null){
			securityManager.setRememberMeManager(rememberMeManager);
		}
		if (sessionManager != null){
			securityManager.setSessionManager(sessionManager);
		}
		if (subjectFactory != null){
			securityManager.setSubjectFactory(subjectFactory);
		}
		if (authenticator != null){
			securityManager.setAuthenticator(authenticator);
		}
		if (authorizer != null){
			securityManager.setAuthorizer(authorizer);
		}
		if (eventBus != null){
			securityManager.setEventBus(eventBus);
		}
		return securityManager;
	}

	/**
	 * 注册一个权限元信息事件的发布器
	 * @param cachingSecurityManager 元信息事件的发布器
	 * @return
	 */
	@Bean
	public AuthMetadataEventPublisher eventBus(CachingSecurityManager cachingSecurityManager){
		return new AuthMetadataEventPublisher(cachingSecurityManager);
	}


	/**
	 * 注册shiroPlus 初始化器
	 * @return
	 */
	@Bean
	public ShiroPlusInitializer shiroPlusInitializer(CachingSecurityManager cachingSecurityManager,
	                                                 AuthMetadataManager authMetadataManager){
		return new ShiroPlusInitializer(cachingSecurityManager , authMetadataManager);
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
	public AuthMetadataManager authMetadataManager(){
		return new AuthMetadataManager(this.metadataLoader);
	}


	@Bean
	@ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
	public ShiroFilterFactoryBean  shiroFilterFactoryBean(SecurityManager sessionManager ,
	                                                      AuthMetadataManager authMetadataManager,
	                                                      ShiroPlusProperties shiroPlusProperties){
		//创建过滤链解析器
		DynamicPathMatchingFilterChainResolver filterChainResolver = new DynamicPathMatchingFilterChainResolver(authMetadataManager);
		if (this.tenantIdGenerator != null){
			filterChainResolver.setTenantIdGenerator(tenantIdGenerator);
		}
		//创建shiro filter bean
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroPlusFilterFactoryBean(filterChainResolver);

		//处理过滤链定义 如配置为 anon -> url 要转为 url -> anon
		Map<String, String> filterChainDefinition = shiroPlusProperties.getFilterChainDefinition();
		if (!MapUtils.isEmpty(filterChainDefinition)){
			Map<String , String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
			for (Map.Entry<String ,String> entry : filterChainDefinition.entrySet()){
				filterChainDefinitionMap.put(entry.getValue() , entry.getKey());
			}
			shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		}
		//设置基本配置
		shiroFilterFactoryBean.setSecurityManager(sessionManager);
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



	@Bean
	public void setEventBus(EventBus eventBus){
		this.eventBus = eventBus;
	}

	/**
	 * 设置租户id生成器
	 * @param tenantIdGenerator 注入租户id生成器
	 */
	@Autowired(required = false)
	public void setTenantIdGenerator(TenantIdGenerator tenantIdGenerator){
		this.tenantIdGenerator = tenantIdGenerator;
	}

	/**
	 * 设置 元数据加载器
	 * @param metadataLoader 注入元数据加载器
	 */
	@Autowired(required = false)
	public void setMetadataLoader(MetadataLoader metadataLoader){
		this.metadataLoader = metadataLoader;
	}


	@Autowired(required = false)
	public void setAuthorizer(Authorizer authorizer){
		this.authorizer = authorizer;
	}

	@Autowired(required = false)
	public void setAuthenticator(Authenticator authenticator){
		this.authenticator = authenticator;
	}


	@Autowired(required = false)
	public void setSubjectFactory(SubjectFactory subjectFactory){
		this.subjectFactory = subjectFactory;
	}

	@Autowired(required = false)
	public void setSessionManager(SessionManager sessionManager){
		this.sessionManager = sessionManager;
	}

	@Autowired(required = false)
	public void setRememberMeManager(RememberMeManager rememberMeManager){
		this.rememberMeManager = rememberMeManager;
	}


	@Autowired(required = false)
	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}


	@Autowired(required = false)
	public void setRealms(List<Realm> realms){
		this.realms.addAll(realms);
	}
}
