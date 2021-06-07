package org.codingeasy.shiroplus.core.event;

import org.apache.shiro.event.Subscribe;
import org.apache.shiro.event.support.AnnotationEventListenerResolver;
import org.apache.shiro.event.support.EventListener;

import java.util.ArrayList;
import java.util.List;

/**
* 通用的事件解析器
 * @see EventListener
 * @see Subscribe
* @author : KangNing Hu
*/
public class CommonEventListenerResolver extends AnnotationEventListenerResolver {


	@Override
	public List<EventListener> getEventListeners(Object instance) {
		List<EventListener> eventListeners = new ArrayList<>();
		//处理 EventListener 接口方式注册的监听器
		if (instance instanceof EventListener){
			eventListeners.add((EventListener)instance);
		}
		//处理 org.apache.shiroplus.event.Subscribe 注解
		eventListeners.addAll(super.getEventListeners(instance));
		return eventListeners;
	}
}
