package me.josephzhu.java8inaction.test;


import me.josephzhu.java8inaction.test.common.Functions;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
    public void batch()
    {
        IntStream.rangeClosed(1, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> Functions.slowEcho.apply(1)).thenAccept(logger::info))
                .toArray();

    }
}
