package org.codingeasy.shiro.example.shiro;

import org.codingeasy.shiro.authorize.metadata.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*   
* @author : KangNing Hu
*/
@Component
public class SimpleMetadataLoader implements MetadataLoader {

	private static final List<PermissionMetadata> ermissionMetadataList = new ArrayList<PermissionMetadata>();

	private static final List<GlobalMetadata> globalMetadata = new ArrayList<GlobalMetadata>();

	static {
		// 权限配置
		ermissionMetadataList.add(new PermissionMetadata("/user" , RequestMethod.POST , Arrays.asList("permi:add"), null , PermiModel.PERMISSION));
		ermissionMetadataList.add(new PermissionMetadata("/user" , RequestMethod.DELETE , Arrays.asList("permi:delete"), null , PermiModel.PERMISSION));
		ermissionMetadataList.add(new PermissionMetadata("/user" , RequestMethod.PUT , Arrays.asList("permi:put"), null , PermiModel.PERMISSION));
		ermissionMetadataList.add(new PermissionMetadata("/user" , RequestMethod.GET , Arrays.asList("permi:get"), null , PermiModel.PERMISSION));
		//全局配置
		globalMetadata.add(new GlobalMetadata(null , Arrays.asList("") , true , true));
	}


	public List<PermissionMetadata> load() {
		return ermissionMetadataList;
	}

	public List<GlobalMetadata> loadGlobal() {
		return globalMetadata;
	}
}
