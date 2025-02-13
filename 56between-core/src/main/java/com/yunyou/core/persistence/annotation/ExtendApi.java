package com.yunyou.core.persistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 外部访问
 *
 * @author WMJ
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtendApi {

    String appId() default "";

    int seconds() default 3;

    int maxCount() default 500;
}