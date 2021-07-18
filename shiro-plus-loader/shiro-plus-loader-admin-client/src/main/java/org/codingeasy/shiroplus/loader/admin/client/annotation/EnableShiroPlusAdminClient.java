package org.codingeasy.shiroplus.loader.admin.client.annotation;

import org.codingeasy.shiroplus.loader.admin.client.configuration.AdminClientAutoConfiguration;
import org.codingeasy.shiroplus.loader.admin.client.configuration.ClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
* 激活admin客服端  
* @author : KangNing Hu
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ClientConfiguration.class , AdminClientAutoConfiguration.class})
public @interface EnableShiroPlusAdminClient {
}
