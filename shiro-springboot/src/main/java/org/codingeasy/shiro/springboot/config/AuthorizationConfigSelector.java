package org.codingeasy.shiro.springboot.config;

import org.apache.commons.collections.MapUtils;
import org.codingeasy.shiro.springboot.AuthorModel;
import org.codingeasy.shiro.springboot.annotaion.EnableShiroPlus;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
* 授权配置选择器  
* @author : KangNing Hu
*/
public class AuthorizationConfigSelector implements ImportSelector {



	private static final String DYNAMIC_AUTHOR_ATTR = "dynamicAuthor";

	private static final String MODEL_ATTR = "model";


	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableShiroPlus.class.getName());
		if (MapUtils.isEmpty(annotationAttributes)){
			return new String[]{AopAuthorizationAutoConfiguration.class.getName()};
		}
		Boolean dynamicAuthor = (Boolean) annotationAttributes.get(DYNAMIC_AUTHOR_ATTR);
		//开启动态授权
		if (dynamicAuthor){
			String[] configClasses = new String[2];
			configClasses[0] = AopAuthorizationAutoConfiguration.class.getName();
			AuthorModel authorModel = (AuthorModel) annotationAttributes.get(MODEL_ATTR);
			if (authorModel == AuthorModel.AOP){
				configClasses[1] = AopDynamicAuthorizationAutoConfiguration.class.getName();
			}else {
				configClasses[1] = DynamicAuthorizationAutoConfiguration.class.getName();
			}
			return configClasses;
		}
		return new String[]{AopAuthorizationAutoConfiguration.class.getName()};
	}
}
