package org.core.common.annotation;

import java.lang.annotation.*;

/**
 * 匿名访问不鉴权注解
 * 谨慎使用此注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnonymousAccess {
}
