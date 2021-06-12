package org.codingeasy.shiroplus.core.annotation;

import java.lang.annotation.*;

/**
 * 动态授权
 * @author kangning <a>2035711178@qq.com</a>
 */
@Target({ElementType.TYPE , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicAuthorization {
}
