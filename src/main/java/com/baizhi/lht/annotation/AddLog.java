package com.baizhi.lht.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AddLog {
    String value() default "";

    String description();
}
