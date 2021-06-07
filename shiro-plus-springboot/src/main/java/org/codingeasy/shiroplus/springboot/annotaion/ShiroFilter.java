package org.codingeasy.shiroplus.springboot.annotaion;

import java.lang.annotation.*;

/**
 * shiro过滤器标识注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ShiroFilter {

	/**
	 * shiroplus 过滤器名称 如 anon
	 * @return
	 */
	String value();
}
