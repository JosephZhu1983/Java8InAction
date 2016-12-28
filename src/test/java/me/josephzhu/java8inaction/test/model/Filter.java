package me.josephzhu.java8inaction.test.model;

import java.lang.annotation.Repeatable;

/**
 * Created by zhuye on 26/12/2016.
 */
@Repeatable(Filters.class)
public @interface Filter
{
    Class<? extends IFilter> value();
}
