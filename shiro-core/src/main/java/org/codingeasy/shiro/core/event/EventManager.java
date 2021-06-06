package org.codingeasy.shiro.core.event;

import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.apache.shiro.event.support.EventListener;
import org.apache.shiro.util.Assert;

import java.util.EventObject;

/**
* 事件管理器
* @author : KangNing Hu
*/
public class EventManager {


	private EventBus eventBus;


	public EventManager(EventBus eventBus ){
		Assert.notNull(eventBus , "事件总线不能为空");
		this.eventBus = eventBus;
	}


	public EventManager(){
		this(createDefaultEventBus());
	}


	/**
	 * 创建默认的事件总线
	 * @return 返回事件总线
	 */
	public static EventBus createDefaultEventBus(){
		DefaultEventBus defaultEventBus = new DefaultEventBus();
		defaultEventBus.setEventListenerResolver(new CommonEventListenerResolver());
		return defaultEventBus;
	}



	/**
	 * 发布事件
	 * @param event 事件对象
	 * @param <T> 事件源类型
	 */
	public <T>void publish(EventObject event){
		eventBus.publish(event);
	}


	/**
	 * 注册一个事件监听器
	 * @param eventListener 监听器对象
	 */
	public void register(EventListener eventListener){
		eventBus.register(eventListener);
	}


	/**
	 * 注册一个事件监听器
	 * @param event 事件对象 可以是{@link EventListener} 也可以是 {@link org.apache.shiro.event.Subscribe}
	 */
	public void register(Object event){
		eventBus.register(event);
	}


	/**
	 * 注销一个事件监听器器
	 * @param eventListener 监听器对象
	 */
	public void unRegister(EventListener eventListener){
		eventBus.unregister(eventListener);
	}

}
