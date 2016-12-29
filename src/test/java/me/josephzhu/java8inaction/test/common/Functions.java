package me.josephzhu.java8inaction.test.common;

import org.jooq.lambda.function.Function2;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * Created by zhuye on 29/12/2016.
 */
public class Functions
{
    private static Random random = new Random();

    public static IntFunction<Integer> doubleService = a ->
    {
        try
        {
            Thread.sleep(random.ints(1,1,10).findFirst().getAsInt() * 100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        int result = a*2;
        return result;
    };

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

    public static IntFunction<Integer> slowEcho = i ->
    {
        try
        {
            Thread.sleep(i*1000);
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
}
