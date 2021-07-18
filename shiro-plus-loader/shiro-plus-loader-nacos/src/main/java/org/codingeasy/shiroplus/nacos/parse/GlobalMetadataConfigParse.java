package org.codingeasy.shiroplus.nacos.parse;

import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventType;
import org.codingeasy.shiroplus.core.metadata.GlobalMetadata;
import org.codingeasy.shiroplus.nacos.metadata.NacosGlobalMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 全局元数据配置解析器  
* @author : kangning <a>2035711178@qq.com</a>
*/
public class GlobalMetadataConfigParse  implements ConfigParse<NacosGlobalMetadata>{

	private static  final Logger logger = LoggerFactory.getLogger(GlobalMetadataConfigParse.class);

	private static final String ATTR_ANONS = "anons";
	private static final String ATTR_ENABLE_AUTHENTICATION = "enable-authentication";
	private static final String ATTR_ENABLE_AUTHORIZATION = "enable-authorization";
	private static final String ATTR_EXTEND = "extend";
	@Override
	public List<NacosGlobalMetadata> parse(Map<String, Object> config, Class<NacosGlobalMetadata> globalMetadataClass) {
		//分组
		Map<String, List<Map.Entry<String, Object>>> group = config
				.entrySet()
				.stream()
				.collect(
						Collectors.groupingBy(item -> {
							String key = item.getKey();
							String[] split = key.split(REG_SEPARATOR);
							if (split.length < 2) {
								logger.warn("无效的配置属性 {}", key);
								return INVALID_KEY;
							}
							return split[0];
						})
				);
		//构建实体
		List<NacosGlobalMetadata> globalMetadataList = new ArrayList<>();
		for (Map.Entry<String , List<Map.Entry<String, Object>>> entry : group.entrySet()){
			if (INVALID_KEY.equals(entry.getKey())){
				continue;
			}
			NacosGlobalMetadata globalMetadata = new NacosGlobalMetadata();
			globalMetadata.setTenantId(entry.getKey());
			List<Map.Entry<String, Object>> value = entry.getValue();
			for (Map.Entry<String, Object> e : value){
				String attrName = StringUtils.substringAfter(e.getKey(), SEPARATOR);
				String valueStr = e.getValue() == null ? "" : e.getValue().toString();
				switch (attrName){
					case ATTR_ANONS:
						globalMetadata.setAnons(Arrays.asList(valueStr.split(ARRAY_SEPARATOR)));
						break;
					case ATTR_ENABLE_AUTHENTICATION:
						globalMetadata.setEnableAuthentication(Boolean.valueOf(valueStr));
						break;
					case ATTR_ENABLE_AUTHORIZATION:
						globalMetadata.setEnableAuthorization(Boolean.valueOf(valueStr));
						break;
					default:
						if (attrName.startsWith(ATTR_EXTEND)){
							String[] split = attrName.split(REG_SEPARATOR);
							if (split.length == 2){
								globalMetadata.setAttr(split[1] , valueStr);
								break;
							}
						}else {
							logger.warn("无效的属性 {}", attrName);
						}
				}
			}
			globalMetadataList.add(globalMetadata);
		}
		return globalMetadataList;
	}

	@Override
	public boolean support(Class tClass) {
		return GlobalMetadata.class.isAssignableFrom(tClass);
	}

	@Override
	public List<NacosGlobalMetadata> comparison(Map<String, Object> oldProperties, Map<String, Object> properties, Class<NacosGlobalMetadata> globalMetadataClass) {
		return doComparison(oldProperties, properties, globalMetadataClass);
	}

	@Override
	public NacosGlobalMetadata createReducedMetadata(String item) {
		NacosGlobalMetadata nacosGlobalMetadata = new NacosGlobalMetadata();
		nacosGlobalMetadata.setTenantId(item);
		nacosGlobalMetadata.setEventType(EventType.DELETE);
		return nacosGlobalMetadata;
	}

	@Override
	public String getKey(NacosGlobalMetadata nacosGlobalMetadata) {
		return nacosGlobalMetadata.getTenantId();
	}
}
