package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.common.Functions;
import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Created by zhuye on 27/12/2016.
 */
public class ParallelStreamTest
{
    @Test
    public void performanceTest()
    {
        System.out.println(Runtime.getRuntime().availableProcessors());
        Functions.calcTime("串行", () -> LongStream.rangeClosed(1, 1000000000L).mapToDouble(Math::sqrt).sum());
        Functions.calcTime("并行", () -> LongStream.rangeClosed(1, 1000000000L).parallel().mapToDouble(Math::sqrt).sum());
    }

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
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");
        Functions.calcTime("并行8并行度", () -> LongStream.rangeClosed(1, 1000000000L).mapToDouble(Math::sqrt).sum());

        IntStream.rangeClosed(1, 10)
                .parallel()
                .forEach(Functions.slowPrint);
    }

    @Test
    public void forEachOrdered()
    {
        IntStream.rangeClosed(1, 5).parallel().forEachOrdered(Functions.slowPrint);
    }
}
