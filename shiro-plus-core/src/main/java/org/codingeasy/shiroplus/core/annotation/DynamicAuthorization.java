package org.codingeasy.shiroplus.core.annotation;

import java.lang.annotation.*;

/**
 * 动态授权
 * @author hukangning
 */
@Target({ElementType.TYPE , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicAuthorization {
}
