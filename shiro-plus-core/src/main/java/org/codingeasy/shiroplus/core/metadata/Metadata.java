package org.codingeasy.shiroplus.core.metadata;

import java.io.Serializable;
import java.util.Map;

/**
* 元信息标识
* @author : kangning <a>2035711178@qq.com</a>
*/
public interface Metadata extends Serializable , Cloneable {


	/**
	 * 设置属性
	 * @param key 属性key
	 * @param value 属性value
	 */
	void setAttr(String key , Object value);

	/**
	 * 设置属性
	 * @param map 待设置的map
	 */
	void setAttr(Map<String , Object> map);
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
