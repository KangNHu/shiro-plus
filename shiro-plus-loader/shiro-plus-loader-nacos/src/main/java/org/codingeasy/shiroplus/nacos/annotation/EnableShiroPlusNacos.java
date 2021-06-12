package org.codingeasy.shiroplus.nacos.annotation;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.codingeasy.shiroplus.nacos.configuration.ShiroPlusNacosAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
* shiro plus nacos 激活注解  
* @author : kangning <a>2035711178@qq.com</a>
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableNacosConfig
@Import(ShiroPlusNacosAutoConfiguration.class)
public @interface EnableShiroPlusNacos {

	/**
	 * 当前注解的属性值
	 */
	String ATTR_PROPERTIES = "value";


	/**
	 * 可以指定nacos配置 如果不指定则使用nacos全局配置
	 * @return
	 */
	NacosProperties  value() default @NacosProperties;
}
