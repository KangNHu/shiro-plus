package org.codingeasy.shiroplus.nacos.parse;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.nacos.metedata.NacosMetadata;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 配置解析器
 * @author hukangning
 */
public interface ConfigParse<T extends NacosMetadata> {

	String SEPARATOR = ".";
	String REG_SEPARATOR = "\\.";
	String ARRAY_SEPARATOR = ",";
	String INVALID_KEY = "_invalid";
	/**
	 * 解析config成实体
	 * @param config 配置map 对象
	 * @param tClass 返回的实体类
	 * @return
	 */
	List<T> parse(Map<String , Object> config  , Class<T> tClass);


	/**
	 * 是否支持
	 * @param tClass 最终的结果类型
	 * @return 如果当前解析器支持则返回true 否则false
	 */
	boolean support(Class tClass);

	/**
	 * 新旧版本配置对比
	 * @param oldProperties 旧配置
	 * @param properties 新配置
	 * @return 返回变更的配置
	 */
	 List<T> comparison(Map<String, Object> oldProperties, Map<String, Object> properties , Class<T> tClass);


	/**
	 * 创建减少的元信息
	 * @param item key
	 * @return 返回减少的元信息
	 */
	T createReducedMetadata(String item);

	/**
	 * 获取权限元数据key
	 * @param nacosMetadata 元数据
	 * @return 返回元数据的key
	 */
	String getKey(T nacosMetadata);


	/**
	 * 执行对比结果
	 * @param oldProperties 旧配置
	 * @param properties 新的配置
	 * @param tClass 配置信息的类型
	 * @return 返回对比后的结果
	 */
	default List<T> doComparison(Map<String, Object> oldProperties, Map<String, Object> properties ,Class<T> tClass ){
		//解析配置
		List<T> oldMetadataList = this.parse(oldProperties, tClass);
		List<T> metadataList =  this.parse(properties, tClass);
		//构建映射
		Map<String, T> oldMap = oldMetadataList.stream().collect(Collectors.toMap(this::getKey, Function.identity()));
		Map<String, T> map = metadataList.stream().collect(Collectors.toMap(this::getKey, Function.identity()));
		//对比处理
		List<T> updateMetadataList = new ArrayList<>();
		Set<String> reducedMetadataList = new HashSet<>(oldMap.keySet());
		reducedMetadataList.removeAll(map.keySet());
		for (T nm : metadataList){
			T oldNm = oldMap.get(getKey(nm));
			if (oldNm == null){
				nm.setEventType(AuthMetadataEvent.EventType.ADD);
				updateMetadataList.add(nm);
				continue;
			}
			if (!oldNm.toString().equals(nm.toString())){
				nm.setEventType(AuthMetadataEvent.EventType.UPDATE);
				updateMetadataList.add(nm);
			}
		}
		//添加减少的元数据
		updateMetadataList.addAll(
				reducedMetadataList
						.stream()
						.map(this::createReducedMetadata)
						.collect(Collectors.toList())
		);
		return updateMetadataList;
	}
}
