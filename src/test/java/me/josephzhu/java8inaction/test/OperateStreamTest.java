package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.collector.BatchCollector;
import me.josephzhu.java8inaction.test.collector.MostPopularCollector;
import me.josephzhu.java8inaction.test.common.Functions;
import me.josephzhu.java8inaction.test.model.Customer;
import me.josephzhu.java8inaction.test.model.Order;
import me.josephzhu.java8inaction.test.model.OrderItem;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhuye on 25/12/2016.
 */
public class OperateStreamTest
{
    private List<Order> orders;

    @Before
    public void data()
    {
        orders = Order.getData();

        orders.forEach(System.out::println);
        System.out.println("==========================================");
    }

    @Test
    public void filter() //筛选
    {
        //最近半年的金额大于40的订单
        orders.stream()
                .filter(order -> order.getPlacedAt().isAfter(LocalDateTime.now().minusMonths(6)))
                .filter(order -> order.getTotalPrice() > 40).forEach(System.out::println);
    }

    @Test
    public void map() //转换
    {
        orders.stream().filter(order -> order.getTotalPrice() > 40)
                .map(order -> new Tuple2<>(order.getCustomerName(), order.getTotalPrice()))
                .forEach(System.out::println);

        //计算所有订单商品数量

        LongAdder longAdder = new LongAdder();
        orders.stream().forEach(order -> order.getOrderItemList().forEach(orderItem -> longAdder.add(orderItem.getProductQuantity())));

        assertThat(longAdder.longValue(), is(orders.stream().mapToLong(order ->
                order.getOrderItemList().stream()
                        .mapToLong(OrderItem::getProductQuantity).sum()).sum()));

        //分组进行处理(还有一个例子演示的是自定义收集器来实现)
        final int BATCH = 10;
        int count = orders.stream().collect(summingInt(order -> order.getOrderItemList().size()));
        System.out.println(String.format("总共%d条记录每次处理%d条", count, BATCH));

        IntStream.range(0, (count + BATCH - 1) / BATCH)
                .mapToObj(i -> orders.stream().flatMap(o -> o.getOrderItemList().stream()).collect(toList())
                        .subList(i * BATCH, Math.min(count, (i + 1) * BATCH)))
                .forEach(Functions.slowPrintList);
    }

    @Test
    public void sort() //排序
    {
        //大于50的订单,按照订单价格倒序前5
        orders.stream().filter(order -> order.getTotalPrice() > 20)
                .sorted((a, b) -> b.getTotalPrice().compareTo(a.getTotalPrice()))
                .map(order -> new Tuple2(order.getCustomerName(), order.getTotalPrice()))
                .limit(5)
                .forEach(System.out::println);
    }

    @Test
    public void flatMap() //扁平化
    {
        System.out.println(orders.stream().mapToDouble(order -> order.getTotalPrice()).sum());
        //直接展开订单商品进行价格统计
        System.out.println(orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .mapToDouble(item -> item.getProductQuantity() * item.getProductPrice()).sum());
    }

    @Test
    public void groupBy() //分组
    {
        //按照下单日期分组
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

        //按照用户名分组,统计下单数量
        System.out.println(orders.stream().collect(groupingBy(Order::getCustomerName, counting()))
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(toList()));

        //按照用户名分组,统计订单总金额
        System.out.println(orders.stream().collect(groupingBy(Order::getCustomerName, summingDouble(Order::getTotalPrice)))
                .entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed()).collect(toList()));

        //按照用户名分组,统计商品采购数量
        System.out.println(orders.stream().collect(groupingBy(Order::getCustomerName,
                summingInt(order -> order.getOrderItemList().stream()
                        .collect(summingInt(OrderItem::getProductQuantity)))))
                .entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(toList()));


        //统计最受欢迎的水果
        orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .collect(groupingBy(OrderItem::getProductName, summingInt(OrderItem::getProductQuantity)))
                .entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .findFirst()
                .ifPresent(System.out::println);

        //统计最受欢迎的水果的另一种方式
        orders.stream()
                .flatMap(order -> order.getOrderItemList().stream())
                .collect(groupingBy(OrderItem::getProductName, summingInt(OrderItem::getProductQuantity)))
                .entrySet().stream().collect(maxBy(Map.Entry.comparingByValue()))
                .ifPresent(System.out::println);


        //按照用户名分组,选用户下的最大的订单
        orders.stream().collect(groupingBy(Order::getCustomerName, collectingAndThen(maxBy(Comparator.comparingDouble(Order::getTotalPrice)), Optional::get)))
                .forEach((k, v) -> System.out.println(k + "#" + v.getTotalPrice() + "@" + v.getPlacedAt()));

        //根据下单年月分组统计订单ID列表
        System.out.println(orders.stream().collect
                (groupingBy(order -> order.getPlacedAt().format(DateTimeFormatter.ofPattern("yyyyMM")),
                        mapping(order -> order.getId(), toList()))));

        //根据下单年月+用户名分组统计订单ID列表
        System.out.println(orders.stream().collect
                (groupingBy(order -> order.getPlacedAt().format(DateTimeFormatter.ofPattern("yyyyMM")),
                        groupingBy(order -> order.getCustomerName(),
                                mapping(order -> order.getId(), toList())))));
    }

    @Test
    public void maxMin() //最大最小
    {
        orders.stream().max(Comparator.comparing(Order::getTotalPrice)).ifPresent(System.out::println);
        orders.stream().min(Comparator.comparing(Order::getTotalPrice)).ifPresent(System.out::println);
    }

    @Test
    public void reduce() //规约
    {
        //订单的总金额
        orders.stream()
                .map(Order::getTotalPrice)
                .reduce(Double::sum)
                .ifPresent(System.out::println);

        //不用reduce的方法
        System.out.println(orders.stream()
                .mapToDouble(Order::getTotalPrice).sum());

        //统计花钱最多的人
        System.out.println(orders.stream().collect(groupingBy(Order::getCustomerName, summingDouble(Order::getTotalPrice)))
                .entrySet().stream()
                .reduce((a, b) -> a.getValue() > b.getValue() ? a : b)
                .map(Map.Entry::getKey).orElse("N/A"));

        //获取最后一个数
        System.out.println(IntStream.rangeClosed(1, 10).reduce((a, b) -> b).orElse(0));
        System.out.println(Stream.empty().reduce((a, b) -> b).orElse(0));

    }

    @Test
    public void distinct() //去重
    {
        //不去重的下单用户
        System.out.println(orders.stream().map(order -> order.getCustomerName()).collect(joining(",")));
        //去重的下单用户
        System.out.println(orders.stream().map(order -> order.getCustomerName()).distinct().collect(joining(",")));
    }

    @Test
    public void collect() //收集
    {
        //用toSet收集器进行去重,然后组合
        System.out.println(orders.stream()
                .map(order -> order.getCustomerName()).collect(toSet())
                .stream().collect(joining(",", "[", "]")));

        //用toCollection收集器指定集合类型
        System.out.println(orders.stream().limit(2).collect(toCollection(LinkedList::new)).getClass());

        //订单平均购买的商品数量
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
        orders.stream().map(order -> order.getCustomerName()).collect(toSet()).forEach(System.out::println);
        //根据是否有下单记录进行分区
        System.out.println(Customer.getData().stream().collect(partitioningBy(customer -> orders.stream().mapToLong(Order::getCustomerId).anyMatch(id -> id == customer.getId()))));
    }

    @Test
    public void skipLimit() //分页
    {
        orders.stream()
                .sorted((a, b) -> b.getPlacedAt().compareTo(a.getPlacedAt()))
                .map(order -> order.getCustomerName() + "@" + order.getPlacedAt())
                .limit(2).forEach(System.out::println);

        orders.stream()
                .sorted((a, b) -> b.getPlacedAt().compareTo(a.getPlacedAt()))
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
