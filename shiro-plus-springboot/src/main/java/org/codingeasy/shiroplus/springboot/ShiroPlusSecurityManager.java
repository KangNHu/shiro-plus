package org.codingeasy.shiroplus.springboot;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
*   
* @author : kangning <a>2035711178@qq.com</a>
*/
public class ShiroPlusSecurityManager extends DefaultWebSecurityManager implements ApplicationListener<ContextRefreshedEvent> {

	private boolean init = false;




	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (init){
			return;
		}
		//初始化组件
		initComponent(event.getApplicationContext());
		init = true;
	}

	/**
	 * 初始化组件
	 * @param applicationContext spring 应用上下文
	 */
	private void initComponent(ApplicationContext applicationContext) {
		// realm list
		String[] realmBeanNames = applicationContext.getBeanNamesForType(Realm.class);
		if (!ArrayUtils.isEmpty(realmBeanNames)){
			this.setRealms(
					Arrays.asList(realmBeanNames)
							.stream()
							.map(beanName -> applicationContext.getBean(beanName, Realm.class))
							.collect(Collectors.toList())
			);
		}
		//cacheManage
		String[] cacheManagerBeanNames = applicationContext.getBeanNamesForType(CacheManager.class);
		if (!ArrayUtils.isEmpty(cacheManagerBeanNames)) {
			this.setCacheManager(applicationContext.getBean(cacheManagerBeanNames[0] , CacheManager.class));
		}
		//rememberMeManager
		String[] rememberMeManagerBeanNames = applicationContext.getBeanNamesForType(RememberMeManager.class);
		if (!ArrayUtils.isEmpty(rememberMeManagerBeanNames)) {
			this.setRememberMeManager(applicationContext.getBean(rememberMeManagerBeanNames[0] , RememberMeManager.class));
		}
		//sessionManager
		SessionManager sessionManager = getValidComponent(applicationContext, SessionManager.class);
		if (sessionManager != null) {
			this.setSessionManager(sessionManager);
		}
		//subjectFactory
		String[] subjectFactoryBeanNames = applicationContext.getBeanNamesForType(SubjectFactory.class);
		if (!ArrayUtils.isEmpty(subjectFactoryBeanNames)) {
			this.setSubjectFactory(applicationContext.getBean(subjectFactoryBeanNames[0] , SubjectFactory.class));
		}
		//authenticator
		Authenticator authenticator = getValidComponent(applicationContext, Authenticator.class);
		if (authenticator != null) {
			this.setAuthenticator(authenticator);
		}
		//authorizer
		Authorizer authorizer = getValidComponent(applicationContext, Authorizer.class);
		if (authorizer != null) {
			this.setAuthorizer(authorizer);
		}
		//eventBus
		String[] eventBusBeanNames = applicationContext.getBeanNamesForType(EventBus.class);
		if (!ArrayUtils.isEmpty(eventBusBeanNames)) {
			this.setEventBus(applicationContext.getBean(eventBusBeanNames[0] , EventBus.class));
		}
		// 设置鉴权策略 当使用默认的鉴权时
		Authenticator defaultAuthenticator = getAuthenticator();
		if (defaultAuthenticator instanceof ModularRealmAuthenticator){
			String[] authenticationStrategyBeanNames = applicationContext.getBeanNamesForType(AuthenticationStrategy.class);
			if (!ArrayUtils.isEmpty(authenticationStrategyBeanNames)) {
				AuthenticationStrategy authenticationStrategy = applicationContext.getBean(authenticationStrategyBeanNames[0], AuthenticationStrategy.class);
				((ModularRealmAuthenticator) defaultAuthenticator).setAuthenticationStrategy(authenticationStrategy);
			}
		}
	}

	/**
	 * 获取有效组件
	 * <p>排除{@link SecurityManager}本身</p>
	 * @param applicationContext spring 上下文
	 * @param componentClass 组件class
	 * @param <T> 组件类型
	 * @return 返回第一个有效的组件
	 */
	private <T> T getValidComponent(ApplicationContext applicationContext,Class<T> componentClass) {
		String[] beanNames = applicationContext.getBeanNamesForType(componentClass);
		return Arrays.asList(beanNames)
					.stream()
					.map(beanName -> applicationContext.getBean(beanName, componentClass))
					.filter(bean -> !(bean instanceof SecurityManager))
					.findFirst()
					.orElse(null);
	}
}
