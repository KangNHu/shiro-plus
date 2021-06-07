package org.codingeasy.shiroplus.springboot.annotaion;

import org.codingeasy.shiroplus.core.interceptor.DynamicAuthorizationFilter;
import org.codingeasy.shiroplus.core.interceptor.aop.AopDynamicAuthorizationInterceptor;
import org.codingeasy.shiroplus.springboot.AuthorModel;
import org.codingeasy.shiroplus.springboot.config.AuthorizationConfigSelector;
import org.codingeasy.shiroplus.springboot.config.ShiroPlusAutoConfiguration;
import org.codingeasy.shiroplus.springboot.config.ShiroPlusSupportConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
* 激活shiro plus  
* @author : KangNing Hu
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ShiroPlusSupportConfiguration.class ,ShiroPlusAutoConfiguration.class , AuthorizationConfigSelector.class})
public @interface EnableShiroPlus {

	/**
	 * 是否开启动态授权
	 * @return
	 */
	boolean dynamicAuthor() default true;


	/**
	 * 是否开启shiro原生授权方式
	 * @return
	 */
	boolean originAuthor() default true;

	/**
	 * 授权模型
	 * @see AopDynamicAuthorizationInterceptor
	 * @see DynamicAuthorizationFilter
	 * @return
	 */
	AuthorModel model() default AuthorModel.WBE;
}
