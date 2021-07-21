package org.codingeasy.shiroplus.loader.admin.server.listener;

import org.codingeasy.shiroplus.core.event.EventType;
import org.codingeasy.shiroplus.loader.admin.server.models.menu.ConfigType;

import java.util.EventObject;

/**
* 配置事件  
* @author : KangNing Hu
*/
public class ConfigEvent extends EventObject {

	/**
	 * 配置类型
	 */
	private ConfigType configType;

	/**
	 * 事件类型
	 */
	private EventType eventType;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public ConfigEvent(Object source) {
		super(source);
	}


	/**
	 * 构建配置事件对象
	 * @param source 事件源
	 * @param configType 配置类型
	 * @param eventType 事件类型
	 * @return
	 */
	public static ConfigEvent build(Object source , ConfigType configType , EventType eventType){
		 ConfigEvent configEvent = new ConfigEvent(source);
		 configEvent.setConfigType(configType);
		 configEvent.setEventType(eventType);
		 return configEvent;
	}

	/**
	 * 权限元信息的新增事件
	 * @param source 事件源
	 * @return 返回事件对象
	 */
	public static ConfigEvent permissionAddEvent(Object source){
		return build(source ,ConfigType.PERMISSION , EventType.ADD );
	}
	/**
	 * 权限元信息的更新事件
	 * @param source 事件源
	 * @return 返回事件对象
	 */
	public static ConfigEvent permissionUpdateEvent(Object source){
		return build(source ,ConfigType.PERMISSION , EventType.UPDATE );
	}

	/**
	 * 权限元信息的更新事件
	 * @param source 事件源
	 * @return 返回事件对象
	 */
	public static ConfigEvent permissionDeleteEvent(Object source){
		return build(source ,ConfigType.PERMISSION , EventType.DELETE );
	}


	/**
	 * 全局元信息的新增事件
	 * @param source 事件源
	 * @return 返回事件对象
	 */
	public static ConfigEvent globalAddEvent(Object source){
		return build(source ,ConfigType.GLOBAL , EventType.ADD );
	}

	/**
	 * 全局元信息的更新事件
	 * @param source 事件源
	 * @return 返回事件对象
	 */
	public static ConfigEvent globalUpdateEvent(Object source){
		return build(source ,ConfigType.GLOBAL , EventType.ADD );
	}

	/**
	 * 全局元信息的删除事件
	 * @param source 事件源
	 * @return 返回事件对象
	 */
	public static ConfigEvent globalDeleteEvent(Object source){
		return build(source ,ConfigType.GLOBAL , EventType.DELETE );
	}


	public ConfigType getConfigType() {
		return configType;
	}

	public void setConfigType(ConfigType configType) {
		this.configType = configType;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
}
