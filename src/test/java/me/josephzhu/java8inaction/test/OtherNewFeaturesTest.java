package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.model.Filter;
import me.josephzhu.java8inaction.test.model.FilterA;
import me.josephzhu.java8inaction.test.model.FilterB;
import org.jooq.lambda.Unchecked;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by zhuye on 26/12/2016.
 */
public class OtherNewFeaturesTest
{
    @Test
    public void defaultMethods()
    {

    }

    @Test
    public void newDate()
    {

    }

    @Test
    public void optional()
    {

    }

    @Test
    public void map()
    {
        Map<String, Long> map = Arrays.asList("aa aa aa bb bb cc".split(" "))
                .stream().collect(Collectors.groupingBy(Function.identity(), counting()));

        assertThat(map.getOrDefault("dd", 0L), is(0L));

        map.putIfAbsent("aa", 1L);//失败
        map.putIfAbsent("dd", 1L);//成功
        assertThat(map.get("aa"), is(3L));
        assertThat(map.get("dd"), is(1L));

        map.replaceAll((k, v) -> k.length() + v);
        map.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    public void atomic()
    {
        DoubleAccumulator da = new DoubleAccumulator((a, b) -> a + b, 1.0);
        da.accumulate(1.0);
        da.accumulate(2.0);
        da.accumulate(3.0);
        assertThat(da.get(), is(7d));

        LongAdder la = new LongAdder();
        la.increment();
        la.decrement();
        la.add(10);
        la.add(5);
        assertThat(la.intValue(), is(15));
    }

    @Test
    @Filter(value = FilterA.class)
    @Filter(value = FilterB.class)
    public void repeatableAnnotation()
    {
        class Local
        {
        }
        ;
        Method m = Local.class.getEnclosingMethod();
        Arrays.stream(m.getAnnotationsByType(Filter.class))
                .map(Unchecked.function(a -> a.value().newInstance()))
                .forEach(c -> c.doWork());
    }
}
