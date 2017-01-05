package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.common.Functions;
import me.josephzhu.java8inaction.test.model.Product;
import org.apache.log4j.Logger;
import org.jooq.lambda.tuple.Tuple3;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.counting;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhuye on 25/12/2016.
 */
public class FunctionalJavaCoolTest
{
    private static Logger logger = Logger.getLogger(FunctionalJavaCoolTest.class);
    private static Random random = new Random();
    private Map<Long, Product> cache = new ConcurrentHashMap<>();

    @Test
    public void lambdaCool()
    {
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        //临时中间集合,这种外循环的代码没有可读性
        List<Point2D> point2DList = new ArrayList<>();
        for (Integer i : ints)
        {
            point2DList.add(new Point2D.Double((double) i % 3, (double) i / 3));
        }
        //临时变量,纯粹是为了获得最后结果需要的中间变量
        double total = 0;
        int count=0;

        for (Point2D point2D : point2DList)
        {
            //过滤
            if (point2D.getY() > 1)
            {
                //算距离
                double distance = point2D.distance(0, 0);
                total += distance;
                count++;
            }
        }

        double average = total / count;

        //如何用一行代码来实现,比较一下可读性
        assertThat(average, is(
                ints.stream()
                .map(i -> new Point2D.Double((double) i % 3, (double) i / 3))
                .filter(point -> point.getY() > 1)
                .mapToDouble(point -> point.distance(0, 0))
                .average()
                .orElse(0)));
    }

    @Test
    public void lambdaGroupByCool()
    {
        List<Tuple3<Integer, Integer, Integer>> data = IntStream.rangeClosed(1, 1000000)
                .mapToObj(i -> new Tuple3<>(
                        random.nextInt(10000),
                        random.nextInt(100000),
                        random.nextInt(100)))
                .collect(Collectors.toList());

        Functions.calcTime("大量数据分组(Stream方式)", () ->
        {
            Map<Integer, List<Tuple3<Integer, Integer, Integer>>> result = data.stream().collect(Collectors.groupingBy(Tuple3::v1));
        });

        Functions.calcTime("大量数据分组(传统方式)", () ->
        {
            Map<Integer, List<Tuple3<Integer, Integer, Integer>>> result = new HashMap<>();
            for (Tuple3<Integer, Integer, Integer> item : data)
            {
                if (!result.containsKey(item.v1))
                    result.put(item.v1, new ArrayList<>());
                result.get(item.v1).add(item);
            }
        });
    }

    @Test
    public void wordCountExample() throws IOException
    {
        Map<String, Long> wordCount = Files.lines(Paths.get("/Users/zhuye/Documents/tb_shipping_order.sql"))
                .parallel()
                .flatMap(line -> Arrays.stream(line.trim().split("\\s")))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())
                .filter(word -> word.length() > 0)
                .map(word -> new AbstractMap.SimpleEntry<>(word, 1))
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, counting()));

        wordCount.forEach((k, v) -> System.out.println(String.format("%s ==>> %d", k, v)));
    }
    @Test
    public void noLoopNoIf() //用Stream来替代循环和判断
    {
        assertThat(getProduct(5),
                is(Product.getData().stream().filter(item -> item.getPrice() > 5).findFirst().get()));

        assertThat(getProduct(10),
                is(Product.getData().stream()
                        .filter(item -> item.getPrice() > 10)
                        .findFirst()
                        .orElse(new Product(0L, "测试", 1.0))));
    }

    private Product getProduct(int price)
    {
        List<Product> productList = Product.getData();
        for (Product product : productList)
        {
            if (product.getPrice() > price)
                return product;
        }
        return new Product(0L, "测试", 1.0);
    }

    @Test
    public void coolCache() //一条语句实现cache的常用模式
    {
        getProductAndCacheCool(1L);
        getProductAndCacheCool(100L);

        System.out.println(cache);
        assertThat(cache.size(), is(1));
        assertTrue(cache.containsKey(1L));
    }

    @Test
    public void notcoolCache()
    {
        getProductAndCache(1L);
        getProductAndCache(100L);

        System.out.println(cache);
        assertThat(cache.size(), is(1));
        assertTrue(cache.containsKey(1L));
    }

    @Test
    public void performanceBenchmark()
    {
        //http://www.oracle.com/technetwork/java/jvmls2013kuksen-2014088.pdf

        Functions.calcTime("lambda", ()-> LongStream.rangeClosed(1,10000000000L).forEach(i->lambda()));
        Functions.calcTime("anonymous", ()->LongStream.rangeClosed(1,10000000000L).forEach(i->anonymous()));
    }

    private Supplier<String> lambda()
    {
        String localString = "test";
        return ()->localString;
    }

    private Supplier <String> anonymous()
    {
        String localString = "test";
        return new Supplier<String>()
        {
            @Override
            public String get()
            {
                return localString;
            }
        };
    }

    private Product getProductAndCacheCool(Long id)
    {
        return cache.computeIfAbsent(id, i ->
                Product.getData().stream().filter(p -> p.getId() == i)
                        .findFirst().orElse(null));
    }

    private Product getProductAndCache(Long id)
    {
        Product product = null;
        if (cache.containsKey(id))
        {
            product = cache.get(id);
        }
        else
        {
            for (Product p : Product.getData())
            {
                if (p.getId().equals(id))
                {
                    product = p;
                    break;
                }
            }
            if (product != null)
                cache.put(id, product);
        }
        return product;
    }

    @Test
    public void loggerCool()
    {
        class Logger
        {
            public boolean isDebugEnabled()
            {
                return false;
            }

            public void info(Supplier<String> message)
            {
                if (isDebugEnabled())
                    logger.info(message.get());
            }
        }

        new Logger().info(() -> "now");
        new Logger().info(() -> "Test" + slowOperation());

    }

    private String slowOperation()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return "slow";
    }

    @Test
    public void notCool()
    {
        try
        {
            IntStream.rangeClosed(-10, 10)
                    .boxed()
                    .filter(i -> 10 / i > 2)
                    .map(Integer::doubleValue)
                    .collect(Collectors.toList());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        //lambda的问题是出了异常比较难排查,唯一的方法就是使用独立的命名函数
        try
        {
            IntStream.rangeClosed(-10, 10)
                    .boxed()
                    .filter(this::condition)
                    .map(Integer::doubleValue)
                    .collect(Collectors.toList());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private boolean condition(int i)
    {
        return 10 / i > 2;
    }
}