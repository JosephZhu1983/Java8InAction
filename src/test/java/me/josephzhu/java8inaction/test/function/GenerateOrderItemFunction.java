package me.josephzhu.java8inaction.test.function;

import me.josephzhu.java8inaction.test.model.OrderItem;
import me.josephzhu.java8inaction.test.model.Product;

/**
 * Created by zhuye on 26/12/2016.
 */

@FunctionalInterface
public interface GenerateOrderItemFunction
{
    OrderItem generate(Product product, int quantity);
}
