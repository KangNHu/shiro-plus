package org.codingeasy.shiroplus.loader.admin.server.security;

import org.apache.commons.lang3.StringUtils;
import org.codingeasy.shiroplus.core.metadata.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
* 基于文件的权限元信息加载器  
* @author : KangNing Hu
*/
@Component
public class FileAuthMetadataLoader  implements MetadataLoader {

	private static final String PERMISSIONS_FILE_PATH = "META-INF/permissions.properties";

	@Override
	public List<PermissionMetadata> load() {
		try {
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PERMISSIONS_FILE_PATH));
			return properties
					.entrySet()
					.stream()
					.map(this::parse)
					.collect(Collectors.toList());
		}catch (Exception e){
			throw new IllegalStateException(e);
		}
	}


	@Override
	public List<GlobalMetadata> loadGlobal() {
		return null;
	}

	/**
	 * 解析entry
	 * @param entry 配置文件的每行属性
	 * @return 返回权限元信息
	 */
	private PermissionMetadata parse(Map.Entry<Object , Object> entry){
		//解析path 和 method
		String key = entry.getKey().toString();
		String path = null;
		RequestMethod method = null;
		String[] split = key.split("\\[|\\]");
		if (split.length >= 2){
			path = split[0];
			method = RequestMethod.form(split[1]);
		}
		//解析permissions
		String value = entry.getValue().toString();
		return new PermissionMetadata(path , method  , Arrays.asList(value), Logical.AND , PermiModel.PERMISSION);
	}
}
