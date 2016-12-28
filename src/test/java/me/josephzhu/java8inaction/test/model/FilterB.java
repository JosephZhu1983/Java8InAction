package me.josephzhu.java8inaction.test.model;

/**
 * Created by zhuye on 26/12/2016.
 */

public class FilterB implements IFilter
{
    @Override
    public void doWork()
    {
        System.out.println("FilterB");
    }
}