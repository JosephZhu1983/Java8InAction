package me.josephzhu.java8inaction.test.function;

import me.josephzhu.java8inaction.test.model.Customer;
import me.josephzhu.java8inaction.test.model.Order;
import me.josephzhu.java8inaction.test.model.Product;

/**
 * Created by zhuye on 26/12/2016.
 */
@FunctionalInterface
public interface GenerateOrderFunction
{
    Order generate(Customer customer, Product product, int quantity);
}