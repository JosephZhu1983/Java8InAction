package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.collector.BatchCollector;
import me.josephzhu.java8inaction.test.collector.MostPopularCollector;
import me.josephzhu.java8inaction.test.common.Functions;
import me.josephzhu.java8inaction.test.function.GenerateOrderFunction;
import me.josephzhu.java8inaction.test.function.GenerateOrderItemFunction;
import me.josephzhu.java8inaction.test.model.Customer;
import me.josephzhu.java8inaction.test.model.Order;
import me.josephzhu.java8inaction.test.model.OrderItem;
import me.josephzhu.java8inaction.test.model.Product;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhuye on 25/12/2016.
 */
public class OperateStreamTest
{
    private static Random random = new Random();
    private List<Order> orders;

    @Before
    public void data()
    {
        orders = Order.getData();

        orders.forEach(System.out::println);
        System.out.println("==========================================");
    }


    @Test
    public void functionalInterfaces()
    {
        //可以看一下java.util.function包
        Supplier<String> supplier = String::new;
        Consumer<String> consumer = System.out::println;
        Predicate<String> predicate = String::isEmpty;
        Function<String, Integer> function = Integer::valueOf;

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

    @Test
    public void filter() //筛选
    {
        System.out.println("//最近半年的金额大于40的订单");
        orders.stream()
                .filter(Objects::nonNull) //注意Objects::nonNull方法
                .filter(order -> order.getPlacedAt().isAfter(LocalDateTime.now().minusMonths(6)))
                .filter(order -> order.getTotalPrice() > 40)
                .map(order -> new Tuple3<>(order.getId(), order.getPlacedAt(), order.getTotalPrice()))
                .forEach(System.out::println);
    }

    @Test
    public void map() //转换
    {
        //计算所有订单商品数量
        LongAdder longAdder = new LongAdder();
        orders.stream().forEach(order ->
                order.getOrderItemList().forEach(orderItem -> longAdder.add(orderItem.getProductQuantity())));
        //mapToLong来实现上面的例子
        assertThat(longAdder.longValue(), is(orders.stream().mapToLong(order ->
                order.getOrderItemList().stream()
                        .mapToLong(OrderItem::getProductQuantity).sum()).sum()));

        //分组进行处理的例子(除了这种实现还可以通过自定义Collector来实现)
        final int BATCH = 10;
        int count = orders.stream().collect(summingInt(order -> order.getOrderItemList().size()));
        System.out.println(String.format("总共%d条记录每次处理%d条", count, BATCH));

        //开始分组处理所有订单中的明细采购记录
        IntStream.rangeClosed(0, count / BATCH)
                .mapToObj(i -> orders.stream().flatMap(o -> o.getOrderItemList().stream()).collect(toList())
                        .subList(i * BATCH, Math.min(count, (i + 1) * BATCH)))
                .forEach(Functions.slowPrintList);
    }

    @Test
    public void sort() //排序
    {
        System.out.println("//大于50的订单,按照订单价格倒序前5");
        orders.stream().filter(order -> order.getTotalPrice() > 50)
                .sorted((a, b) -> b.getTotalPrice().compareTo(a.getTotalPrice()))
                .map(order -> new Tuple2(order.getId(), order.getTotalPrice()))
                .limit(5)
                .forEach(System.out::println);

        System.out.println("******");

        orders.stream().filter(order -> order.getTotalPrice() > 50)
                .sorted(comparing(Order::getTotalPrice).reversed())
                .map(order -> new Tuple2(order.getId(), order.getTotalPrice()))
                .limit(5)
                .forEach(System.out::println);
    }

    @Test
    public void flatMap() //扁平化
    {
        System.out.println(orders.stream().mapToDouble(order -> order.getTotalPrice()).sum());

        //如果不依赖订单上的总价格,可以直接展开订单商品进行价格统计
        System.out.println(orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .mapToDouble(item -> item.getProductQuantity() * item.getProductPrice()).sum());

        //另一种方式flatMap+mapToDouble=flatMapToDouble
        System.out.println(orders.stream()
                .flatMapToDouble(order ->
                        order.getOrderItemList()
                                .stream().mapToDouble(item -> item.getProductQuantity() * item.getProductPrice()))
                .sum());
    }

    @Test
    public void groupBy() //分组
    {
        System.out.println("//按照下单日期分组");
        System.out.println(orders.stream().map(order -> new Tuple2<>(order.getId(), order.getPlacedAt()))
                .collect(groupingBy(order ->
                {
                    if (order.v2.isBefore(LocalDateTime.now().minusMonths(6)))
                        return "6个月之前";
                    else if (order.v2.isBefore(LocalDateTime.now().minusMonths(3))
                            && order.v2.isAfter(LocalDateTime.now().minusMonths(6)))
                        return "3个月之前";
                    else if (order.v2.isAfter(LocalDateTime.now().minusMonths(3)))
                        return "最近3个月";
                    return "其他";
                })));

        System.out.println("//按照用户名分组,统计下单数量");
        //groupingBy下游是counting
        System.out.println(orders.stream().collect(groupingBy(Order::getCustomerName, counting()))
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(toList()));

        System.out.println("//按照用户名分组,统计订单总金额");
        //groupingBy下游是summingDouble
        System.out.println(orders.stream().collect(groupingBy(Order::getCustomerName, summingDouble(Order::getTotalPrice)))
                .entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed()).collect(toList()));

        System.out.println("//按照用户名分组,统计商品采购数量");
        System.out.println(orders.stream().collect(groupingBy(Order::getCustomerName,
                summingInt(order -> order.getOrderItemList().stream()
                        .collect(summingInt(OrderItem::getProductQuantity)))))
                .entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(toList()));

        System.out.println("//统计最受欢迎的水果,排序后取第一个");
        orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .collect(groupingBy(OrderItem::getProductName, summingInt(OrderItem::getProductQuantity)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(System.out::println);

        System.out.println("//统计最受欢迎的水果的另一种方式,直接利用maxBy");
        orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .collect(groupingBy(OrderItem::getProductName, summingInt(OrderItem::getProductQuantity)))
                .entrySet().stream()
                .collect(maxBy(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .ifPresent(System.out::println);


        System.out.println("//按照用户名分组,选用户下的最大的订单");
        orders.stream().collect(groupingBy(Order::getCustomerName, collectingAndThen(maxBy(comparingDouble(Order::getTotalPrice)), Optional::get)))
                .forEach((k, v) -> System.out.println(k + "#" + v.getTotalPrice() + "@" + v.getPlacedAt()));

        System.out.println("//根据下单年月分组统计订单ID列表");
        System.out.println(orders.stream().collect
                (groupingBy(order -> order.getPlacedAt().format(DateTimeFormatter.ofPattern("yyyyMM")),
                        mapping(order -> order.getId(), toList()))));

        System.out.println("//根据下单年月+用户名分组统计订单ID列表");
        System.out.println(orders.stream().collect
                (groupingBy(order -> order.getPlacedAt().format(DateTimeFormatter.ofPattern("yyyyMM")),
                        groupingBy(order -> order.getCustomerName(),
                                mapping(order -> order.getId(), toList())))));
    }

    @Test
    public void maxMin() //最大最小
    {
        orders.stream().max(comparing(Order::getTotalPrice)).ifPresent(System.out::println);
        orders.stream().min(comparing(Order::getTotalPrice)).ifPresent(System.out::println);
    }

    @Test
    public void reduce() //规约
    {
        System.out.println("//订单的总金额");
        orders.stream()
                .map(Order::getTotalPrice)
                .reduce(Double::sum)
                .ifPresent(System.out::println);

        System.out.println("//订单的总金额不用reduce的方法");
        System.out.println(orders.stream()
                .mapToDouble(Order::getTotalPrice).sum());

        System.out.println("//统计花钱最多的人");
        System.out.println(orders.stream().collect(groupingBy(Order::getCustomerName, summingDouble(Order::getTotalPrice)))
                .entrySet().stream()
                .reduce(BinaryOperator.maxBy(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey).orElse("N/A"));

        System.out.println("//获取最后一个数,一个利用reduce投巧的例子");
        System.out.println(IntStream.rangeClosed(1, 10).reduce((a, b) -> b).orElse(0));
        System.out.println(Stream.empty().reduce((a, b) -> b).orElse(0));

    }

    @Test
    public void distinct() //去重
    {
        System.out.println("//不去重的下单用户");
        System.out.println(orders.stream().map(order -> order.getCustomerName()).collect(joining(",")));

        System.out.println("//去重的下单用户");
        System.out.println(orders.stream().map(order -> order.getCustomerName()).distinct().collect(joining(",")));

        System.out.println("//所有购买过的商品");
        System.out.println(orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .map(OrderItem::getProductName)
                .distinct().collect(joining(",")));
    }

    @Test
    public void collect() //收集
    {
        System.out.println("//生成一定位数的随机字符串");
        System.out.println(random.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(20)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString());

        System.out.println("//所有下单的用户,使用toSet去重了");
        System.out.println(orders.stream()
                .map(order -> order.getCustomerName()).collect(toSet())
                .stream().collect(joining(",", "[", "]")));

        System.out.println("//用toCollection收集器指定集合类型");
        System.out.println(orders.stream().limit(2).collect(toCollection(LinkedList::new)).getClass());

        System.out.println("//使用toMap获取订单ID+下单用户名的Map");
        orders.stream()
                .collect(toMap(Order::getId, Order::getCustomerName))
                .entrySet().forEach(System.out::println);

        System.out.println("//使用toMap获取下单用户名(因为可能有重复)+最近一次下单时间的Map");
        orders.stream()
                .collect(toMap(Order::getCustomerName, Order::getPlacedAt, (x, y) -> x.isAfter(y) ? x : y))
                .entrySet().forEach(System.out::println);

        System.out.println("//订单平均购买的商品数量");
        System.out.println(orders.stream().collect(averagingInt(order ->
                order.getOrderItemList().stream()
                        .collect(summingInt(OrderItem::getProductQuantity)))));
    }

    @Test
    public void customCollector() //自定义收集器
    {
        //最受欢迎收集器
        assertThat(Stream.of(1, 1, 2, 2, 2, 3, 4, 5, 5).collect(new MostPopularCollector<>()).get(), is(2));
        assertThat(Stream.of('a', 'b', 'c', 'c', 'c', 'd').collect(new MostPopularCollector<>()).get(), is('c'));

        //分批收集器
        IntStream.rangeClosed(1, 24).boxed().collect(new BatchCollector<>(5, System.out::println));
    }

    @Test
    public void partition() //分区
    {
        //先来看一下所有下单的用户
        orders.stream().map(order -> order.getCustomerName()).collect(toSet()).forEach(System.out::println);
        //根据是否有下单记录进行分区
        System.out.println(Customer.getData().stream().collect(
                partitioningBy(customer -> orders.stream().mapToLong(Order::getCustomerId)
                        .anyMatch(id -> id == customer.getId()))));
    }

    @Test
    public void skipLimit() //分页
    {
        orders.stream()
                .sorted(comparing(Order::getPlacedAt))
                .map(order -> order.getCustomerName() + "@" + order.getPlacedAt())
                .limit(2).forEach(System.out::println);

        orders.stream()
                .sorted(comparing(Order::getPlacedAt))
                .map(order -> order.getCustomerName() + "@" + order.getPlacedAt())
                .skip(2).limit(2).forEach(System.out::println);
    }

    @Test
    public void match() //匹配
    {
        assertTrue(orders.stream().anyMatch(order -> order.getOrderItemList().size() > 5));
        assertTrue(orders.stream().allMatch(order -> order.getOrderItemList().size() > 0));
        assertTrue(orders.stream().noneMatch(order -> order.getCustomerName().equals("储骏")));
    }

    @Test
    public void peek() //调试
    {
        orders.stream()
                .filter(order -> order.getTotalPrice() > 40)
                .peek(order -> System.out.println(order.getTotalPrice()))
                .collect(toList());
    }
}
