package me.josephzhu.java8inaction.test.model;

import lombok.Data;

/**
 * Created by zhuye on 03/01/2017.
 */
@Data
public class TVResult<T>
{
    private Integer error_code;
    private String reason;
    private T result;
}
