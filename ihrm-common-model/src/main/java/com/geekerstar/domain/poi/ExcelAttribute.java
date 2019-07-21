package com.geekerstar.domain.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAttribute {
    /**
     * 对应的列名称
     */
    String name() default "";

    /**
     * excel列的索引
     */
    int sort();

    /**
     * 字段类型对应的格式
     */
    String format() default "";
}
