package me.josephzhu.java8inaction.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhuye on 25/12/2016.
 */
@Data
@AllArgsConstructor
public class Customer
{
    private Long id;
    private String name;

    public static List<Customer> getData()
    {
        return Arrays.asList(
                new Customer(10L, "垂建"),
                new Customer(11L, "红涛"),
                new Customer(12L, "宇峰"),
                new Customer(13L, "王希"),
                new Customer(14L, "志抄"),
                new Customer(15L, "维维"),
                new Customer(16L, "雪源"),
                new Customer(17L, "碰碰"),
                new Customer(18L, "正龙"),
                new Customer(19L, "舒扬"),
                new Customer(20L, "重庆")
        );
    }
}
