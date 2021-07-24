package org.codingeasy.shiroplus.core.metadata;


import org.codingeasy.shiroplus.core.utils.CopyUtils;

import java.util.HashMap;
import java.util.Map;

/**
* 抽象的元信息  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class AbstractMetadata implements Metadata {

	private HashMap<String , Object> extend = new HashMap<>();


	public HashMap<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(HashMap<String, Object> extend) {
		this.extend = extend;
	}

	/**
	 * 设置属性
	 * @param key 属性key
	 * @param value 属性value
	 */
	public void setAttr(String key , Object value){
		this.extend.put(key  ,value);
	}

	/**
	 * 设置属性
	 * @param map 待设置的map
	 */
	public void setAttr(Map<String , Object> map){
		this.extend.putAll(map);
	}


	/**
	 * 获取属性
	 * @param key 属性key
	 * @param <T> 属性值类型
	 * @return 如果有对应的属性则返回对应的属性值，否则返回null
	 */
	public <T> T get(String key){
		return get(key , null);
	}

	/**
	 * 获取属性
	 * @param key 属性key
	 * @param defaultValue 默认值
	 * @param <T> 属性值类型
	 * @return 如果有对应的属性则返回对应的属性值 ，否则返回默认值
	 */
	public <T>T get(String key  , T defaultValue){
		Object value = this.extend.get(key);
		return value == null ? defaultValue : (T) value;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		AbstractMetadata clone = (AbstractMetadata) super.clone();
		clone.setAttr((HashMap<String, Object>)CopyUtils.copyMap(this.extend));
		return clone;
	}

	@Override
	public String toString() {
		return "AbstractMetadata{" +
				"extend=" + extend +
				'}';
	}
}
