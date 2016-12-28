package me.josephzhu.java8inaction.test.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhuye on 26/12/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Filters
{
    Filter[] value();
}