package org.codingeasy.shiroplus.springboot.config;

import org.apache.shiro.util.Assert;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.core.Ordered;

/**
* 事件监听器自动注册器
 * @see org.apache.shiro.event.Subscribe
 * @see org.apache.shiro.event.support.EventListener
* @author : KangNing Hu
*/
public class EventListenerAutoRegistry implements SmartInstantiationAwareBeanPostProcessor, Ordered {

	private EventManager eventManager;

	public EventListenerAutoRegistry(EventManager eventManager){
		Assert.notNull(eventManager , "事件管理器不能为空");
		this.eventManager = eventManager;
	}


	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		if (AopUtils.isAopProxy(bean) || AopUtils.isCglibProxy(bean) || isSkip(bean)){
			return false;
		}
		//注册事件
		eventManager.register(bean);
		return false;
	}


	/**
	 * 是否跳过注册
	 * @param bean spring bean
	 * @return 如果跳过则true 否则返回false
	 */
	private boolean isSkip(Object bean) {
		Class<?> aClass = bean.getClass();
		String typeName = aClass.getTypeName();
		return typeName.startsWith("java") ||
				typeName.startsWith("javax") ||
				typeName.startsWith("org.springframework")||
				typeName.startsWith("org.apache");
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
