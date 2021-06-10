package org.codingeasy.shiroplus.nacos.parse;

import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.metadata.Logical;
import org.codingeasy.shiroplus.core.metadata.PermiModel;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.codingeasy.shiroplus.core.metadata.RequestMethod;
import org.codingeasy.shiroplus.nacos.metedata.NacosMetadata;
import org.codingeasy.shiroplus.nacos.metedata.NacosPermissionMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* 权限元信息配置解析器  
* @author : KangNing Hu
*/
public class PermissionMetadataConfigParse implements ConfigParse<NacosPermissionMetadata> {

	private static final Logger logger = LoggerFactory.getLogger(PermissionMetadataConfigParse.class);


	private static final String ATTR_PERMIS = "permis";
	private static final String ATTR_LOGICAL = "logical";
	private static final String ATTR_PERMI_MODEL = "permi-model";

	@Override
	public List<NacosPermissionMetadata> parse(Map<String, Object> config, Class<NacosPermissionMetadata> permissionMetadataClass) {
		//分组结果
		Map<String, List<Map.Entry<String, Object>>> group = config
				.entrySet()
				.stream()
				.collect(
						Collectors.groupingBy(
								item -> {
									String[] split = item.getKey().split(REG_SEPARATOR);
									if (split.length != 3) {
										logger.warn("无效的配置属性 {}" , item.getKey());
										return INVALID_KEY;
									}
									return split[0] + SEPARATOR + split[1];
								}
						)
				);
		//构建实体
		List<NacosPermissionMetadata> permissionMetadataList = new ArrayList<>();
		for (Map.Entry<String , List<Map.Entry<String , Object>>> entry : group.entrySet()){
			String key = entry.getKey();
			List<Map.Entry<String, Object>> value = entry.getValue();
			if (INVALID_KEY.equals(key)){
				continue;
			}
			String[] split = key.split(REG_SEPARATOR);
			NacosPermissionMetadata permissionMetadata = new NacosPermissionMetadata();
			permissionMetadata.setPath(split[0]);
			permissionMetadata.setMethod(RequestMethod.form(split[1]));
			for (Map.Entry<String , Object> e : value){
				String attrName = StringUtils.substringAfterLast(e.getKey(),SEPARATOR);
				Object v = e.getValue();
				String valueStr = v == null ? "" : v.toString();
				switch (attrName){
					case ATTR_LOGICAL:
						permissionMetadata.setLogical(Logical.form(valueStr));
						break;
					case ATTR_PERMI_MODEL:
						permissionMetadata.setPermiModel(PermiModel.form(valueStr));
						break;
					case ATTR_PERMIS:
						permissionMetadata.setPermis(Arrays.asList(valueStr.split(ARRAY_SEPARATOR)));
						break;
					default:
						logger.warn("无效的配置属性 {}" , e.getKey());
				}
			}
			//校验元数据是否合法
			if (permissionMetadata.getMethod() == null){
				logger.warn("无效的配置信息 {}" , permissionMetadata);
				continue;
			}
			permissionMetadataList.add(permissionMetadata);

		}
		return permissionMetadataList;
	}

	@Override
	public boolean support(Class tClass) {
		return PermissionMetadata.class.isAssignableFrom(tClass);
	}

	@Override
	public List<NacosPermissionMetadata> comparison(Map<String, Object> oldProperties, Map<String, Object> properties, Class<NacosPermissionMetadata> permissionMetadataClass) {
		return doComparison(oldProperties , properties , permissionMetadataClass);
	}

	@Override
	public NacosPermissionMetadata createReducedMetadata(String item) {
		String[] split = item.split(REG_SEPARATOR);
		NacosPermissionMetadata permissionMetadata = new NacosPermissionMetadata();
		permissionMetadata.setPath(split[0]);
		permissionMetadata.setMethod(RequestMethod.form(split[1]));
		permissionMetadata.setEventType(AuthMetadataEvent.EventType.DELETE);
		return permissionMetadata;
	}


	/**
	 * 获取权限元数据key
	 * @param permissionMetadata 权限元数据
	 * @return
	 */
	public String getKey(NacosPermissionMetadata permissionMetadata){
		return permissionMetadata.getPath() + SEPARATOR + permissionMetadata.getMethod().name();
	}
}
