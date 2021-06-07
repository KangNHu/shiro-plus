package org.codingeasy.shiroplus.core.event;

import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.apache.shiro.event.support.EventListener;
import org.apache.shiro.util.Assert;

import java.util.EventObject;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
* 事件管理器
* @author : KangNing Hu
*/
public class EventManager {


	private EventBus eventBus;


	private Executor executor;

	public EventManager(EventBus eventBus ){
		Assert.notNull(eventBus , "事件总线不能为空");
		this.eventBus = eventBus;
		this.executor = createDefaultExecutor();
	}


	public EventManager(){
		this(createDefaultEventBus());
	}

	/**
	 * 创建默认线程迟
	 * @return 返回一个线程池
	 */
	public  Executor createDefaultExecutor(){
		return new ThreadPoolExecutor(4,
				Runtime.getRuntime().availableProcessors() +1,
				50000,
				TimeUnit.MILLISECONDS,
				new LinkedBlockingDeque<>(),
				new ThreadFactory() {
					private AtomicInteger count = new AtomicInteger(0);

					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r ,"Event-Thread-" + count.incrementAndGet());
					}
				});
	}


	public void setExecutor(Executor executor) {
		Assert.notNull(executor , "设置的 executor 不能为空");
		this.executor = executor;
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
	 */
	public void publish(EventObject event){
		eventBus.publish(event);
	}


	/**
	 * 异步发布事件
	 * @param event 事件对象
	 */
	public void asyncPublish(EventObject event){
		executor.execute(() -> this.eventBus.publish(event));
	}


	/**
	 * 异步发布事件
	 * @param event 事件对象
	 * @param executor 异步处理的线程池
	 */
	public void asyncPublish(EventObject event , Executor executor){
		executor.execute(() -> this.eventBus.publish(event));
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
