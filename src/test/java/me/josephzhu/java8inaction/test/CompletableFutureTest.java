package me.josephzhu.java8inaction.test;


import me.josephzhu.java8inaction.test.common.Functions;
import me.josephzhu.java8inaction.test.model.Customer;
import me.josephzhu.java8inaction.test.model.Order;
import me.josephzhu.java8inaction.test.model.Product;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhuye on 26/12/2016.
 */
public class CompletableFutureTest
{
    private static Logger logger = Logger.getLogger(CompletableFutureTest.class);

    @Test
    public void basic()
    {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> Functions.slowEcho.apply(1));

        try
        {
            logger.info(completableFuture.getNow(-1));
            logger.info(completableFuture.get());
        } catch (Exception e)
        {
            logger.error("错误", e);
        }
    }

    @Test
    public void timeout()
    {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> Functions.slowEcho.apply(1));

        try
        {
            logger.info(completableFuture.get(500, TimeUnit.MILLISECONDS));
        } catch (Exception e)
        {
            logger.error("错误", e);
        }
    }

    @Test
    public void chain()
    {
        Functions.calcTime("下单链式操作", () ->
        {
            CompletableFuture.supplyAsync(() -> Customer.getRandomCustomer())
                    .thenCombine(CompletableFuture.supplyAsync(() -> Product.getRandomProduct()),
                            ((customer, product) ->
                            {
                                if (customer == null || product == null)
                                    return null;
                                return Order.placeOrder(customer, product);
                            })).thenAccept(System.out::println).join();
        });
    }
}
