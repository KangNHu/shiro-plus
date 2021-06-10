package org.codingeasy.shiroplus.nacos.parse;


import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.util.ConfigParseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;

import java.util.*;

/**
* 配置解析的委托器
 * @see ConfigParse
* @author : KangNing Hu
*/
public class ConfigParseDelegate {


	private List<ConfigParse>  configParses = new ArrayList<>();


	private Map<Class , Map<String , Object>> currentVersionData = new HashMap<>();


	public ConfigParseDelegate(){
		addDealtConfigParses();
	}

	/**
	 * 添加配置解析器
	 * @param configParse 配置解析器对象
	 */
	public void addConfigParse(ConfigParse configParse){
		this.configParses.add(configParse);
	}



	/**
	 * 添加默认的继解析器
	 */
	private void addDealtConfigParses(){
		this.addConfigParse(new GlobalMetadataConfigParse());
		this.addConfigParse(new PermissionMetadataConfigParse());
	}

	/**
	 * 解析配置
	 * @param config 配置字符串
	 * @param tClass 需要解析的class 类型
	 * @param configType 配置的数据类型
	 */
	public <T>List<T> parse(String config , Class<T> tClass , ConfigType configType){
		return parse(config , tClass , configType , false);
	}


	/**
	 * 解析配置
	 * @param config 配置字符串
	 * @param tClass 需要解析的class 类型
	 * @param isUpdate 是否更新 用于配置变更
	 * @param configType 配置的数据类型
	 */
	public <T>List<T> parse(String config , Class<T> tClass , ConfigType configType , boolean isUpdate){
		//配置预处理
		Map<String, Object> properties = pretreatment(config ,configType );
		List<T> list = null;
		//非更新操作
		if (!isUpdate) {
			list = doParse(properties, tClass);
		}
		synchronized (this) {
			if (list == null) {
				//更新操作
				Map<String, Object> oldProperties = this.currentVersionData.get(tClass);
				//如果不存在在则直接返回
				if (oldProperties == null) {
					list = doParse(properties, tClass);
				}
				//对比新旧配置
				else {
					list = doComparison(oldProperties, properties, tClass);
				}
			}
			this.currentVersionData.put(tClass , properties);
			return list;
		}
	}

	/**
	 * 配置预处理
	 * @param config 配置文本
	 * @param configType 配置格式类型
	 * @return
	 */
	private Map<String, Object> pretreatment(String config, ConfigType configType) {
		Map<String, Object> pretreatmentProperties = ConfigParseUtils.toProperties(config, configType.getType());
		return findChildMap("" , pretreatmentProperties);
	}

	/**
	 * 查找字map
	 * <p>
	 *     如 key1 -> {key2 -> value}
	 *     则最后的结果为 key1.key2 -> vaue
	 * </p>
	 * @param parentKey 父级 key
	 * @param parentValue 父级 value
	 * @return 返回所有的子属性
	 */
	private Map<String, Object> findChildMap(String parentKey, Map<String ,Object> parentValue) {
		Map<String , Object> childMap = new LinkedHashMap<>();
		for (Map.Entry<String , Object> entry : parentValue.entrySet()){
			Object value = entry.getValue();
			if (value instanceof  Map){
				childMap.putAll(findChildMap(entry.getKey() , (Map)value));
			}else {
				childMap.put((StringUtils.isEmpty(parentKey) ? "" : (parentKey + ".")) + entry.getKey() , value);
			}
		}
		return childMap;
	}


	/**
	 *对比新旧版本的值
	 * @param oldProperties 旧版本值
	 * @param properties 新版本的值
	 * @param tClass 最终结果的元素类型
	 * @return 返回对比后的值
	 */
	private <T> List<T> doComparison(Map<String, Object> oldProperties,
	                                         Map<String, Object> properties , Class tClass ) {
		for (ConfigParse configParse : configParses){
			if (configParse.support(tClass)){
				return configParse.comparison(oldProperties , properties , tClass);
			}
		}
		return new ArrayList<>();
	}

	/**
	 * 执行解析
	 * @param properties 配置对象
	 * @param tClass 最终结果类型的class
	 * @param <T>  最终结果类型
	 * @return 返回一个列表
	 */
	private <T> List<T> doParse(Map<String, Object> properties, Class<T> tClass) {
		Assert.isTrue(!configParses.isEmpty() , "没有找到可用的配置解析器");
		if (properties.isEmpty()){
			return new ArrayList<>();
		}
		for (ConfigParse configParse : configParses){
			if (configParse.support(tClass)){
				return configParse.parse(properties , tClass);
			}
		}
		throw new  IllegalArgumentException("没有找到可用的配置解析器");
	}


	public static void main(String[] args) {
		Map<String , Object> map = new LinkedHashMap<>();
		map.put("111" , "ssssss");
		Map<String , Object> map1 = new LinkedHashMap<>();
		map1.put("aaaaa" , "111111");
		map1.put("bbbbb" , "222222");
		map.put("child" , map1);
		ConfigParseDelegate configParseDelegate = new ConfigParseDelegate();
		Map<String, Object> childMap = configParseDelegate.findChildMap("", map);
		System.out.println(childMap.toString());
	}
}
