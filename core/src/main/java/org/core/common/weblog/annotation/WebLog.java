package org.core.common.weblog.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 前端打印注解, 使用于Controller
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WebLog {
    /**
     * 日志名称，用于区分模块
     */
    String value() default "";
}
