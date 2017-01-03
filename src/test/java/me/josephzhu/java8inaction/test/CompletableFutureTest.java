package me.josephzhu.java8inaction.test;


import me.josephzhu.java8inaction.test.collector.BatchCollector;
import me.josephzhu.java8inaction.test.common.Functions;
import me.josephzhu.java8inaction.test.model.Customer;
import me.josephzhu.java8inaction.test.model.Order;
import me.josephzhu.java8inaction.test.model.Product;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Created by zhuye on 26/12/2016.
 */
public class CompletableFutureTest
{
    private static Random random = new Random();
    private static Logger logger = Logger.getLogger(CompletableFutureTest.class);
    private static ExecutorService executors = Executors.newFixedThreadPool(10, r ->
    {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("zhuye");
        return thread;
    });
    private Map<Long, FraudState> orders = new ConcurrentHashMap<>();

    @Before
    public void initOrders()
    {
        orders = LongStream.rangeClosed(1, 100)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), l -> FraudState.unchecked));
    }

    @Test
    public void basic() //基本使用
    {
        CompletableFuture<Integer> completableFuture1 = new CompletableFuture<>();
        Future<Integer> future = executors.submit(() -> Functions.slowEcho.apply(1));
        try
        {
            completableFuture1.complete(future.get());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            logger.info(completableFuture1.get());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> Functions.slowEcho.apply(1));

        try
        {
            logger.info(completableFuture2.getNow(-1)); //一开始获取不到,用默认值
            logger.info(completableFuture2.get()); //等待并获取结果
        }
        catch (Exception e)
        {
            logger.error("错误", e);
        }
    }

    @Test
    public void timeout() //演示超时
    {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> Functions.slowEcho.apply(1));

        try
        {
            logger.info(completableFuture.get(500, TimeUnit.MILLISECONDS)); //超时
        }
        catch (Exception e)
        {
            logger.error("错误", e);
        }
    }

    @Test
    public void customThreadPool() //自定义线程池
    {
        Functions.calcTime("parallel", () ->
                IntStream.rangeClosed(1, 10)
                        .parallel()
                        .forEach(i -> logger.info(Functions.slowEcho.apply(i))));

        Functions.calcTime("默认", () ->
                IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> CompletableFuture.runAsync(() -> logger.info(Functions.slowEcho.apply(i))))
                        .collect(Collectors.toList())
                        .forEach(CompletableFuture::join));

        Functions.calcTime("自定义线程池", () ->
                IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> CompletableFuture.runAsync(() -> logger.info(Functions.slowEcho.apply(i)), executors))
                        .collect(Collectors.toList())
                        .forEach(CompletableFuture::join));
    }

    @Test
    public void chain() //链式异步操作
    {
        Functions.calcTime("三种执行方式", () ->
        {
            CompletableFuture.completedFuture(1).thenAccept(logger::info).join();
            CompletableFuture.completedFuture(1).thenAcceptAsync(logger::info).join();
            CompletableFuture.completedFuture(1).thenAcceptAsync(logger::info, executors).join();
        });

        Functions.calcTime("acceptEither的例子", () ->
                doubleService(1).acceptEither(doubleService(2), logger::info).join());

        Functions.calcTime("thenCompose的例子", () -> echoService(1)
                .thenCompose(this::doubleService)
                .thenCompose(this::doubleService)
                .thenCompose(this::doubleService)
                .whenComplete(logger::info).join());

        Functions.calcTime("thenCombine下单的例子", () ->
                CompletableFuture.supplyAsync(() -> Customer.getRandomCustomer())
                        .thenCombine(CompletableFuture.supplyAsync(() -> Product.getRandomProduct()),
                                ((customer, product) -> Order.placeOrder(customer, product)))
                        .thenAccept(logger::info)
                        .join());
    }

    private CompletableFuture<Integer> doubleService(int n)
    {
        return CompletableFuture.supplyAsync(() -> Functions.doubleService.apply(n));
    }

    private CompletableFuture<Integer> echoService(int n)
    {
        return CompletableFuture.supplyAsync(() -> Functions.slowEcho.apply(n));
    }

    @Test
    public void updateFraudDataExample()
    {
        //一行代码实现:
        //第一步:获取待处理的订单:需要1秒
        //第二步:分批查询订单的反欺诈状态:分成5批,需要5秒获取数据
        //第三步:更新订单的反欺诈状态:10线程每秒可以保存20单,需要5秒
        //由于第二步是并行的,独立的线程池,所以整体不需要11秒的时间

        System.out.println(orders.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting())));

        Functions.calcTime("批量同步欺诈状态到运单", () ->
            getToBeProcessedOrders().stream().collect(new BatchCollector<>(20, orderIds ->
                    CompletableFuture.allOf(
                        CompletableFuture.supplyAsync(() -> getFraudStateBatch(orderIds))
                            .thenAccept
                            (
                                data -> data.entrySet().stream().map
                                        (item -> CompletableFuture.runAsync
                                            (() -> updateOrderFraudState(item.getKey(),
                                                    item.getValue().orElse(FraudState.unknown)), executors)
                                        ).collect(Collectors.toList())
                            )
                    ).join())
            )
        );

        //getToBeProcessedOrders().stream().collect(new BatchCollector<>(10, orderIds -> CompletableFuture.allOf(CompletableFuture.supplyAsync(() -> getFraudStateBatch(orderIds)).thenAccept(data -> data.entrySet().stream().map(item -> CompletableFuture.runAsync(() -> updateOrderFraudState(item.getKey(), item.getValue().orElse(FraudState.unknown)), executors)).collect(Collectors.toList()))).join()));

        System.out.println(orders.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting())));
    }

    private List<Long> getToBeProcessedOrders()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return orders.keySet().stream().collect(Collectors.toList());
    }

    private Map<Long, Optional<FraudState>> getFraudStateBatch(List<Long> orderIds)
    {
        logger.info("getFraudStateBatch:" + orderIds);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return orderIds.stream().collect(Collectors.toMap(Function.identity(), id ->
        {
            int i = random.nextInt(10);
            if (i == 0)
                return Optional.empty();
            else if (i >= 8)
                return Optional.of(FraudState.is_fraud);
            else
                return Optional.of(FraudState.not_fraud);
        }));
    }

    private void updateOrderFraudState(long id, FraudState state)
    {
        logger.info("updateOrderFraudState:" + id);
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        orders.put(id, state);
    }

    enum FraudState
    {
        unchecked,
        is_fraud,
        not_fraud,
        unknown,
    }
}
