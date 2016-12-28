package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.function.GenerateOrderFunction;
import me.josephzhu.java8inaction.test.function.GenerateOrderItemFunction;
import me.josephzhu.java8inaction.test.model.Customer;
import me.josephzhu.java8inaction.test.model.Order;
import me.josephzhu.java8inaction.test.model.OrderItem;
import me.josephzhu.java8inaction.test.model.Product;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhuye on 25/12/2016.
 */
public class FunctionalJavaCoolTest
{
    private Map<Long, Product> cache = new HashMap<>();

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

    @Test
    public void coolCache() //一条语句实现cache的常用模式
    {
        getProductAndCache(1L);
        getProductAndCache(100L);

        System.out.println(cache);
        assertThat(cache.size(), is(1));
        assertTrue(cache.containsKey(1L));
    }

    private void getProductAndCache(Long id)
    {
        cache.computeIfAbsent(id, i -> Product.getData().stream().filter(p -> p.getId() == i).findFirst().orElse(null));
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
    public void loggerCool()
    {
        class Logger
        {
            public boolean isDebugEnabled()
            {
                return false;
            }

            public void debug(Supplier<String> message)
            {
                if (isDebugEnabled())
                    System.out.println(message.get());
            }
        }

        new Logger().debug(() -> "Test" + slowOperation());
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
    public void highOrderFunction() // 高阶函数
    {
        Function<Function<Integer, Integer>, Function<Integer, Integer>> twice = f -> f.andThen(f);
        assertThat(twice.apply(x -> x + 3).apply(7), is(13));
    }

    @Test
    public void functionalInterfaces()
    {
        //可以看一下java.util.function包

        //Predicate的例子
        Predicate<Integer> positiveNumber = i -> i > 0;
        Predicate<Integer> evenNumber = i -> i % 2 == 0;
        assertTrue(positiveNumber.and(evenNumber).test(2));

        //Consumer的例子
        Consumer<String> println = System.out::println;
        println.andThen(println).accept("abcdefg");

        //Function的例子
        Function<String, String> upperCase = String::toUpperCase;
        Function<String, String> duplicate = s -> s.concat(s);
        assertThat(upperCase.andThen(duplicate).apply("test"), is("TESTTEST"));

        //Supplier的例子
        Supplier<Integer> random = () -> new Random().nextInt();
        System.out.println(random.get());

        //BiFunction的例子
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        BiFunction<Integer, Integer, Integer> subtraction = (a, b) -> a - b;
        assertThat(subtraction.apply(add.apply(1, 2), 3), is(0));

        //自定义functional interface的例子
        ToDoubleBiFunction<Product, Integer> calcPrice = (product, quantity) -> product.getPrice() * quantity;
        GenerateOrderItemFunction generateOrderItemFunction = (product, quantity) -> new OrderItem(product.getId(), product.getName(), product.getPrice(), quantity);
        GenerateOrderFunction generateOrderFunction = (customer, product, quantity) ->
        {
            Order o = new Order();
            o.setPlacedAt(LocalDateTime.now());
            o.setOrderItemList(new ArrayList<>());
            o.setCustomerName(customer.getName());
            o.setCustomerId(customer.getId());
            o.setTotalPrice(product.getPrice());
            o.getOrderItemList().add(generateOrderItemFunction.generate(product, quantity));
            o.setTotalPrice(calcPrice.applyAsDouble(product, quantity));
            return o;
        };

        System.out.println(generateOrderFunction.generate(Customer.getData().get(0), Product.getData().get(0), 2));

    }
}