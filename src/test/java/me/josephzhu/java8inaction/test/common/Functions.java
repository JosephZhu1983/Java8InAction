package me.josephzhu.java8inaction.test.common;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

/**
 * Created by zhuye on 29/12/2016.
 */
public class Functions
{
    public static IntConsumer slowPrint = i ->
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println(i);
    };
    ;
    public static IntFunction<Integer> slowEcho = i ->
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return i;
    };
    public static Consumer<List> slowPrintList = list ->
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println(String.format("处理完成:%d条", list.size()));
    };
    private static Random random = new Random();
    public static IntFunction<Integer> doubleService = a ->
    {
        try
        {
            Thread.sleep(random.ints(1, 1, 10).findFirst().getAsInt() * 100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        int result = a * 2;
        return result;
    };

    public static void calcTime(String name, Runnable job)
    {
        Instant start = Instant.now();
        job.run();
        System.out.println(String.format("执行【%s】耗时:%d毫秒", name, Duration.between(start, Instant.now()).toMillis()));
    }
}
