package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.common.Functions;
import me.josephzhu.java8inaction.test.model.TVCategory;
import me.josephzhu.java8inaction.test.model.TVChannel;
import me.josephzhu.java8inaction.test.model.TVService;
import org.apache.log4j.Logger;
import org.jooq.lambda.Unchecked;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Created by zhuye on 27/12/2016.
 */
public class ParallelStreamTest
{
    private static String serviceKey = "671ce07390f367bc490d82f8aec9fd82";
    private TVService service = null;
    private static Logger logger = Logger.getLogger(ParallelStreamTest.class);

    @Test
    public void performanceTest() //串行和并行的性能测试
    {
        System.out.println(Runtime.getRuntime().availableProcessors());
        Functions.calcTime("串行", () -> LongStream.rangeClosed(1, 1000000000L).mapToDouble(Math::sqrt).sum());
        Functions.calcTime("并行", () -> LongStream.rangeClosed(1, 1000000000L).parallel().mapToDouble(Math::sqrt).sum());
    }

    @Test
    public void parallel()
    {
        IntStream.rangeClosed(1, 10)
                .parallel()
                .sequential()
                .parallel() //以最后的为准
                .forEach(Functions.slowPrint);

        Arrays.asList("a1", "a2", "a3")
                .parallelStream() //直接parallelStream也可以
                .forEach(System.out::println);

    }

    @Test
    public void parallelism() //修改并行度
    {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");

        IntStream.rangeClosed(1, 10)
                .parallel()
                .forEach(i->logger.info(Functions.slowEcho.apply(i)));

        Functions.calcTime("并行8并行度", () -> LongStream.rangeClosed(1, 1000000000L).mapToDouble(Math::sqrt).sum());
        //执行【串行】耗时:9841毫秒
        //执行【并行】耗时:3233毫秒
        //执行【并行8并行度】耗时:9209毫秒
        //由于只有4个核,所以对于CPU bound的操作,提高并行度只会增加线程上下文切换的成本,不会带来任何的性能优势

    }

    @Test
    public void forEachOrdered()
    {
        IntStream.rangeClosed(1, 5).parallel().forEachOrdered(Functions.slowPrint);
        //一旦强制有序,并行处理就会失效
    }

    @Before
    public void initService()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://japi.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(TVService.class);
    }

    @Test
    public void tvServiceExample() throws IOException
    {
        //获取所有的电视节目信息
        //第一步:获取所有分类
        //第二步:获取所有频道
        //第三步:获取所有节目

        Functions.calcTime("串行", Unchecked.runnable(() ->
        {
            System.out.println(service.getCategories(serviceKey)
                    .execute().body().getResult()
                    .stream()
                    .collect(Collectors.toMap(
                            TVCategory::getName,
                            Unchecked.function(category ->
                                    service.getChannels(serviceKey, category.getId())
                                            .execute().body().getResult()
                                            .stream()
                                            .collect(Collectors.toMap(TVChannel::getChannelName,
                                                    Unchecked.function(channel ->
                                                            service.getPrograms(serviceKey, channel.getPId(), DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()))
                                                                    .execute().body().getResult()
                                                                    .entrySet()
                                                                    .stream()
                                                                    .map(Map.Entry::getValue)
                                                                    .collect(Collectors.toList()))
                                            )))
                    )));
        }));

        Functions.calcTime("并行", Unchecked.runnable(() ->
        {
            System.out.println(service.getCategories(serviceKey)
                    .execute().body().getResult()
                    .parallelStream()
                    .collect(Collectors.toMap(
                            TVCategory::getName,
                            Unchecked.function(category ->
                                    service.getChannels(serviceKey, category.getId())
                                            .execute().body().getResult()
                                            .parallelStream()
                                            .collect(Collectors.toMap(TVChannel::getChannelName,
                                                    Unchecked.function(channel ->
                                                            service.getPrograms(serviceKey, channel.getPId(), DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()))
                                                                    .execute().body().getResult()
                                                                    .entrySet()
                                                                    .stream() //这个操作不涉及耗时的IO操作,不需要并行处理
                                                                    .map(Map.Entry::getValue)
                                                                    .collect(Collectors.toList()))
                                            )))
                    )));
        }));
    }
}
