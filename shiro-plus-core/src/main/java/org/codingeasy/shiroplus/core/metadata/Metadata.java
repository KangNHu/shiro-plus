package org.codingeasy.shiroplus.core.metadata;

import java.io.Serializable;

/**
* 元信息  
* @author : KangNing Hu
*/
public interface Metadata extends Serializable , Cloneable {


	/**
	 * 设置属性
	 * @param key 属性key
	 * @param value 属性value
	 */
	void setAttr(String key , Object value);


	/**
	 * 获取属性
	 * @param key 属性key
	 * @param <T> 属性值类型
	 * @return 如果有对应的属性则返回对应的属性值，否则返回null
	 */
	<T> T get(String key);

	/**
	 * 获取属性
	 * @param key 属性key
	 * @param defaultValue 默认值
	 * @param <T> 属性值类型
	 * @return 如果有对应的属性则返回对应的属性值 ，否则返回默认值
	 */
	<T>T get(String key  , T defaultValue);

}
