package me.josephzhu.java8inaction.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zhuye on 25/12/2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem
{
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer productQuantity;
}
