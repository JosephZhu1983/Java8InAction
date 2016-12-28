package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.common.Functions;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * Created by zhuye on 27/12/2016.
 */
public class ParallelStreamTest
{
    @Test
    public void forEach()
    {
        IntStream.rangeClosed(1, 10)
                .parallel()
                .sequential()
                .parallel()
                .forEach(Functions.slowPrint);
    }

    @Test
    public void parallelism()
    {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
        IntStream.rangeClosed(1, 10).parallel().forEach(Functions.slowPrint);
    }

    @Test
    public void forEachOrdered()
    {
        IntStream.rangeClosed(1, 10).parallel().forEachOrdered(Functions.slowPrint);
    }
}
