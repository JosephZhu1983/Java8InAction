package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.common.Functions;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by zhuye on 25/12/2016.
 */
public class GenerateStreamTest
{
    private static Logger logger = Logger.getLogger(GenerateStreamTest.class);

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

        //转换
        System.out.println(IntStream.of(1, 2).toArray().getClass()); //class [I
        System.out.println(Stream.of(1, 2).mapToInt(Integer::intValue).toArray().getClass()); //class [I
        System.out.println(IntStream.of(1, 2).boxed().toArray().getClass()); //class [Ljava.lang.Object;
        System.out.println(IntStream.of(1, 2).asDoubleStream().toArray().getClass()); //class [D
        System.out.println(IntStream.of(1, 2).asLongStream().toArray().getClass()); //class [J

        IntStream.range(1, 3).forEach(System.out::println);
        IntStream.range(0, 3).mapToObj(i -> "x").forEach(System.out::println);

        IntStream.rangeClosed(1, 3).forEach(System.out::println);
        DoubleStream.of(1.1, 2.2, 3.3).forEach(System.out::println);

        Arrays.asList("a", "b", "c").stream()   // Stream<String>
                .mapToInt(String::length)       // IntStream
                .asLongStream()                 // LongStream
                .mapToDouble(x -> x / 10.0)     // DoubleStream
                .boxed()                        // Stream<Double>
                .mapToLong(x -> 1L)             // LongStream
                .mapToObj(x -> "")              // Stream<String>
                .collect(Collectors.toList());
    }

    @Test
    public void primitiveStreamPerformanceBenchmark() //性能测试
    {
        Functions.calcTime("装箱拆箱", () ->
        {
            for (int k = 0; k < 10000000; k++)
            {
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).stream()
                        .map(i -> i + 1)
                        .max(Integer::max)
                        .get();
            }
        });

        Functions.calcTime("基本数据", () ->
        {
            for (int k = 0; k < 10000000; k++)
            {
                IntStream.rangeClosed(1, 10)
                        .map(i -> i + 1)
                        .max();
            }
        });
    }

    @Test
    public void concat() //流合并
    {
        Stream.concat(Stream.of(4, 2, 1), IntStream.of(3, 5, 6).boxed())
                .sorted().forEach(System.out::println);
    }

    @Test
    public void filesBasic() throws IOException
    {
        Files.walk(Paths.get("/Users/zhuye/Downloads"), 2)
                .limit(100)
                .map(String::valueOf)
                .filter(path -> !path.startsWith("."))
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }


    @Test
    public void grepContentFromFileExample() throws IOException
    {
        //实现一个类似grep的例子,查找当前目录所有java文件中带有class的行
        Pattern pattern = Pattern.compile("class");
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");
        try (Stream<Path> pathStream = Files.walk(Paths.get(".")))
        {
            pathStream.filter(Files::isRegularFile)
                    .filter(pathMatcher::matches)
                    .flatMap(path ->
                    {
                        try
                        {
                            return Files.readAllLines(path).stream()
                                    .filter(line -> pattern.matcher(line).find())
                                    .map(line -> path + ":" + line);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                            return Stream.of("");
                        }
                    })
                    .forEach(logger::info);
        }
    }
}
