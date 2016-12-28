package me.josephzhu.java8inaction.test.model;

/**
 * Created by zhuye on 26/12/2016.
 */

public class FilterA implements IFilter
{
    @Override
    public void doWork()
    {
        System.out.println("FilterA");
    }
}