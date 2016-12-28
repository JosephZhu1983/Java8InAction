package me.josephzhu.java8inaction.test.common;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

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
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println(i);
    };

    public static Consumer<List> slowPrintList = list ->
    {
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println(String.format("处理完成:%d条", list.size()));
    };
}
