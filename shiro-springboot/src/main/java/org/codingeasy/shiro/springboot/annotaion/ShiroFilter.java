package org.codingeasy.shiro.springboot.annotaion;

import java.lang.annotation.*;

/**
 * shiro过滤器标识注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ShiroFilter {
	String value();
}
