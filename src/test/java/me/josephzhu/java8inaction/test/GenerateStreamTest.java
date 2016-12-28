package me.josephzhu.java8inaction.test;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by zhuye on 25/12/2016.
 */
public class GenerateStreamTest
{
    @Test
    public void of()
    {
        String[] arr = {"a", "b", "c"};
        Stream.of(arr).forEach(System.out::println);
        Stream.of("a", "b", "c").forEach(System.out::println);
        Stream.of(1, 2, "a").map(item -> item.getClass().getName()).forEach(System.out::println);
    }

    @Test
    public void iterate()
    {
        Stream.iterate(2, item -> item * 2).limit(10).forEach(System.out::println);
        Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.TEN)).limit(10).forEach(System.out::println);
    }

    @Test
    public void generate()
    {
        Stream.generate(() -> "test").limit(3).forEach(System.out::println);
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }

    @Test
    public void stream()
    {
        Arrays.asList("a1", "a2", "a3").stream().forEach(System.out::println);
        Arrays.stream(new int[]{1, 2, 3}).forEach(System.out::println);
    }

    @Test
    public void primitive() //基本数据流
    {
        System.out.println(new Random().ints(10, 1, 20)
                .mapToObj(i -> String.valueOf(i))
                .collect(Collectors.joining(",")));

        System.out.println(IntStream.of(1, 2).boxed().toArray().getClass());
        System.out.println(IntStream.of(1, 2).toArray().getClass());

        IntStream.range(1, 3).forEach(System.out::println);
        IntStream.range(0, 10).mapToObj(i -> "x").forEach(System.out::println);

        IntStream.rangeClosed(1, 3).forEach(System.out::println);
        DoubleStream.of(1.1, 2.2, 3.3).forEach(System.out::println);
    }

    @Test
    public void concat() //流合并
    {
        Stream.concat(Stream.of(4, 2, 1), IntStream.of(3, 5, 6).boxed())
                .sorted().forEach(System.out::println);
    }

    @Test
    public void files() throws IOException
    {
        Files.list(Paths.get("/Users/zhuye/Downloads"))
                .limit(10)
                .map(String::valueOf)
                .filter(path -> !path.startsWith("."))
                .sorted()
                .forEach(System.out::println);
    }
}
