package org.codingeasy.shiroplus.core.metadata;


import java.util.HashMap;

/**
* 抽象的元信息  
* @author : KangNing Hu
*/
public class AbstractMetadata implements Metadata {

	private HashMap<String , Object> attr = new HashMap<>();

	/**
	 * 设置属性
	 * @param key 属性key
	 * @param value 属性value
	 */
	public void setAttr(String key , Object value){
		this.attr.put(key  ,value);
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
		Object value = this.attr.get(key);
		return value == null ? defaultValue : (T) value;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		AbstractMetadata clone = (AbstractMetadata) super.clone();
		clone.attr = (HashMap<String, Object>) this.attr.clone();
		return clone;
	}
}
