package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.model.Product;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhuye on 25/12/2016.
 */
public class FunctionalJavaCoolTest
{
    private static Logger logger = Logger.getLogger(FunctionalJavaCoolTest.class);
    private Map<Long, Product> cache = new HashMap<>();

    @Test
    public void lambdaCool()
    {
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        //临时集合
        List<Point2D> point2DList = new ArrayList<>();
        for (Integer i : ints)
        {
            point2DList.add(new Point2D.Double((double) i % 3, (double) i / 3));
        }
        //默认值
        double max = 0;
        for (Point2D point2D : point2DList)
        {
            //过滤
            if (point2D.getY() > 1)
            {
                //算距离
                double distance = point2D.distance(0, 0);
                //比较
                max = Math.max(max, distance);
            }
        }

        assertThat(max, is(ints.stream()
                .map(i -> new Point2D.Double((double) i % 3, (double) i / 3))
                .filter(point -> point.getY() > 1)
                .mapToDouble(point -> point.distance(0, 0))
                .max().orElse(0)));
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
    public void notcoolCache() //一条语句实现cache的常用模式
    {
        getProductAndCache(1L);
        getProductAndCache(100L);

        System.out.println(cache);
        assertThat(cache.size(), is(1));
        assertTrue(cache.containsKey(1L));
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
        } else
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
                return true;
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
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return "slow";
    }

    @Test
    public void highOrderFunction() throws Exception // 高阶函数
    {
        //返回一个函数的函数
        Callable<Runnable> test = () -> () -> System.out.println("hi");
        test.call().run();

        //输入一个函数,返回这个函数执行两次的函数
        Function<Function<Integer, Integer>, Function<Integer, Integer>> twice = f -> f.andThen(f);
        assertThat(twice.apply(x -> x + 3).apply(7), is(13));
    }
}