package org.codingeasy.shiro.springboot.annotaion;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * shiro过滤器标识注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ShiroFilter {

	/**
	 * shiro 过滤器名称 如 anon
	 * @return
	 */
	String value();
}
