package org.codingeasy.shiroplus.nacos.configuration;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.spring.beans.factory.annotation.ConfigServiceBeanBuilder;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.MetadataLoader;
import org.codingeasy.shiroplus.nacos.annotation.EnableShiroPlusNacos;
import org.codingeasy.shiroplus.nacos.metedata.NacosMetadataLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Set;

/**
* shiro pllu集成 nacos自动装配类
 * <p>因为{@link ConfigServiceBeanBuilder} 并没有注册为bean定义而是注册为spring 单例对象
 * 即{@link org.springframework.beans.factory.support.DefaultListableBeanFactory#registerSingleton(String, Object)}
 * 因此这里不能在{@link ConditionalOnBean#value()}加{@link ConfigServiceBeanBuilder}条件
 * </p>
* @author : KangNing Hu
*/
@Configuration
@ConditionalOnBean(value = EventManager.class)
public class ShiroPlusNacosAutoConfiguration implements ImportAware{


	private Map<String , Object> nacosProperties;


	@Bean
	@ConfigurationProperties("shiroplus.nacos")
	public ShiroPlusNacosProperties shiroPlusNacosProperties(){
		return new ShiroPlusNacosProperties();
	}


	@Bean
	@Primary
	public MetadataLoader metadataLoader(ShiroPlusNacosProperties shiroPlusNacosProperties,
	                                     ConfigServiceBeanBuilder configServiceBeanBuilder ,
	                                     EventManager eventManager, Environment environment){
		return new NacosMetadataLoader(configServiceBeanBuilder.build(nacosProperties),shiroPlusNacosProperties,eventManager);
	}


	@Override
	public void setImportMetadata(AnnotationMetadata annotationMetadata) {
		Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableShiroPlusNacos.class.getName());
		this.nacosProperties = (Map<String, Object>) annotationAttributes.get(EnableShiroPlusNacos.ATTR_PROPERTIES);
	}
}
