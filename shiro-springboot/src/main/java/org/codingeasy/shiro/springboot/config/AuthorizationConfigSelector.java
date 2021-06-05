package org.codingeasy.shiro.springboot.config;

import org.apache.commons.collections.MapUtils;
import org.codingeasy.shiro.springboot.AuthorModel;
import org.codingeasy.shiro.springboot.annotaion.EnableShiroPlus;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* 授权配置选择器  
* @author : KangNing Hu
*/
public class AuthorizationConfigSelector implements ImportSelector {



	private static final String DYNAMIC_AUTHOR_ATTR = "dynamicAuthor";

	private static final String MODEL_ATTR = "model";

	private static final String ORIGIN_AUTHOR_ATTR = "originAuthor";


	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableShiroPlus.class.getName());
		Boolean dynamicAuthor = (Boolean) annotationAttributes.get(DYNAMIC_AUTHOR_ATTR);
		Boolean originAuthor = (Boolean) annotationAttributes.get(ORIGIN_AUTHOR_ATTR);
		List<String> classNames = new ArrayList<>();
		if (originAuthor){
			classNames.add(AopAuthorizationAutoConfiguration.class.getName());
		}
		//开启动态授权
		if (dynamicAuthor){
			AuthorModel authorModel = (AuthorModel) annotationAttributes.get(MODEL_ATTR);
			if (authorModel == AuthorModel.AOP){
				classNames.add(AopDynamicAuthorizationAutoConfiguration.class.getName());
			}else {
				classNames.add(DynamicAuthorizationAutoConfiguration.class.getName());
			}
		}
		return classNames.toArray(new String[]{});
	}
}
