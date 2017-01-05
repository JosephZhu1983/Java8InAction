package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.model.Filter;
import me.josephzhu.java8inaction.test.model.FilterA;
import me.josephzhu.java8inaction.test.model.FilterB;
import me.josephzhu.java8inaction.test.model.Product;
import org.jooq.lambda.Unchecked;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Created by zhuye on 26/12/2016.
 */
public class OtherNewFeaturesTest
{
    private ThreadLocal<List<String>> listThreadLocal = ThreadLocal.withInitial(ArrayList::new);

    @Test
    public void newLocalDate()
    {
        //创建
        System.out.println(LocalDate.now());
        System.out.println(LocalDate.of(1983, 02, 17));
        System.out.println(LocalDate.of(1983, Month.FEBRUARY, 17));

        //演示操作
        System.out.println(LocalDate.of(1983, 02, 17).plusYears(30));
        System.out.println(LocalDate.now().minus(Period.ofDays(1)));

        //各种例子
        System.out.println("//今年的程序员日");
        System.out.println(LocalDate.of(LocalDate.now().getYear(), 1, 1).plusDays(256));

        System.out.println("//本月的第一天");
        System.out.println(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));

        System.out.println("//今天之前的一个周六");
        System.out.println(LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SATURDAY)));

        System.out.println("//本月最后一个工作日");
        System.out.println(LocalDate.now().with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY)));
    }

    @Test
    public void newLocalDateTime()
    {
        //转换
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        System.out.println(LocalDateTime.now().getYear());
        System.out.println(LocalDateTime.now().getMonthValue());
        System.out.println(LocalDateTime.now().getDayOfMonth());
        System.out.println(LocalDateTime.now().getHour());
        System.out.println(LocalDateTime.now().getMinute());
        System.out.println(LocalDateTime.now().getSecond());
        //演示操作
        System.out.println(LocalDateTime.now().plus(Period.ofDays(1)).minus(Duration.ofHours(12)));
    }

    @Test
    public void newZonedDateTime()
    {
        ZoneId.getAvailableZoneIds().forEach(id -> System.out.println(String.format("%s:%s", id, ZonedDateTime.now(ZoneId.of(id)))));
    }

    @Test
    public void newDateTimeFormatter()
    {
        System.out.println(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.CHINESE)
                .format(LocalDateTime.now()));

        System.out.println(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.CHINESE)
                .format(LocalDateTime.now()));

        System.out.println(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
                .withLocale(Locale.CHINESE)
                .format(LocalDateTime.now()));

        //演示DateTimeFormatter的各种Pattern
        System.out.println(DateTimeFormatter.ofPattern("yy yyyy").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("M MM MMM MMMM").withLocale(Locale.CHINESE).format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("d dd").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("e E EEEE").withLocale(Locale.CHINESE).format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("H HH").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("mm").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("ss").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("z x").format(ZonedDateTime.now())); //必须是ZonedDateTime才能输出时区信息

        //使用DateTimeFormatterBuilder比记住各种演示DateTimeFormatter的各种Pattern的Pattern更直观
        System.out.println(new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR)
                .appendLiteral("/")
                .appendValue(ChronoField.MONTH_OF_YEAR)
                .appendLiteral("/")
                .appendValue(ChronoField.DAY_OF_YEAR)
                .appendLiteral(" ")
                .appendValue(ChronoField.CLOCK_HOUR_OF_DAY)
                .appendLiteral(":")
                .appendValue(ChronoField.MINUTE_OF_HOUR)
                .appendLiteral(":")
                .appendValue(ChronoField.SECOND_OF_MINUTE)
                .toFormatter().format(LocalDateTime.now()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void optional()
    {
        assertThat(Optional.of(1).get(), is(1));

        //Optional的各种链式处理
        assertThat(Optional.ofNullable(null).orElse("A"), is("A"));

        assertFalse(OptionalDouble.empty().isPresent());

        assertThat(Optional.of(1).map(Math::incrementExact).get(), is(2));

        assertThat(Optional.of(1).filter(integer -> integer % 2 == 0).orElse(null), is(nullValue()));

        Optional.empty().orElseThrow(IllegalArgumentException::new);
    }

    @Test
    public void optionalCool()
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("a", "1");
        properties.put("b", "b");
        properties.put("c", "-1");

        //Optional链式处理的一个例子
        assertThat(readPositiveNumberCool(properties, "a"), is(readPositiveNumber(properties, "a")));//1
        assertThat(readPositiveNumberCool(properties, "b"), is(readPositiveNumber(properties, "b")));//0
        assertThat(readPositiveNumberCool(properties, "c"), is(readPositiveNumber(properties, "c")));//0
    }

    private int readPositiveNumber(Map<String, String> properties, String name)
    {
        String value = properties.get(name);
        if (value != null)
        {
            try
            {
                Integer integer = Integer.parseInt(value);
                if (integer > 0)
                    return integer;
            }
            catch (NumberFormatException ex)
            {
            }
        }
        return 0;
    }

    private int readPositiveNumberCool(Map<String, String> properties, String name)
    {
        return Optional.ofNullable(properties.get(name))
                .flatMap(this::parseInt)
                .filter(i -> i > 0)
                .orElse(0);
    }

    private Optional<Integer> parseInt(String s)
    {
        try
        {
            return Optional.of(Integer.parseInt(s));
        }
        catch (NumberFormatException ex)
        {
            return Optional.empty();
        }
    }

    @Test
    public void map() //map的新增方法
    {
        Map<String, Long> map = Arrays.asList("aa aa aa bb bb cc".split(" "))
                .stream().collect(Collectors.groupingBy(Function.identity(), counting()));

        assertThat(map.getOrDefault("dd", 0L), is(0L)); //演示getOrDefault

        map.putIfAbsent("aa", 1L);//失败
        map.putIfAbsent("dd", 1L);//成功
        assertThat(map.get("aa"), is(3L));
        assertThat(map.get("dd"), is(1L));

        map.replaceAll((k, v) -> k.length() + v); //演示replaceAll
        map.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    public void atomic()
    {
        AtomicLong al = new AtomicLong();
        assertThat(al.updateAndGet(x -> x + 1), is(1L));
        assertThat(al.getAndAdd(2), is(1L));

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
    public void repeatableAnnotation() //可重复的注解,是不是直观很多?
    {
        class Local
        {
        }

        Method m = Local.class.getEnclosingMethod();
        Arrays.stream(m.getAnnotationsByType(Filter.class))
                .map(Unchecked.function(a -> a.value().newInstance()))
                .forEach(c -> c.doWork());
    }

    @Test
    public void miscFeatures()
    {
        //ThreadLocal初始化更简单了
        listThreadLocal.get().add("a");
        assertThat(listThreadLocal.get().get(0), is("a"));

        //按照商品名和价格排序
        Stream.of(new Product(1L, "bb", 1.0), new Product(2L, "aa", 2.0), new Product(3L, "bb", 2.0), new Product(4L, "bb", 1.5))
                .sorted(Comparator.comparing(Product::getName)
                        .thenComparingDouble(Product::getPrice))
                .forEachOrdered(System.out::println);
    }

    @Test
    public void testMovingAverage()  //基于时间窗口的平均值(用Arrays.parallelPrefix来实现)
    {
        System.out.println(
                DoubleStream.of(movingAverage(new double[]{0, 1, 2, 3, 4, 3.5}, 3))
                        .boxed().map(String::valueOf).collect(joining(",")));
    }

    private double[] movingAverage(double[] list, int window)
    {
        double[] sums = Arrays.copyOf(list, list.length);
        Arrays.parallelPrefix(sums, Double::sum);
        System.out.println(DoubleStream.of(sums).boxed().map(String::valueOf).collect(joining(",")));
        int start = window - 1;
        return IntStream.range(start, sums.length)
                .mapToDouble(i ->
                {
                    double prefix = i == start ? 0 : sums[i - window];
                    return (sums[i] - prefix) / window;
                }).toArray();
    }

    @Test
    public void nashorn() throws FileNotFoundException, ScriptException, NoSuchMethodException
    {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader("script.js"));

        Invocable invocable = (Invocable) engine;

        Object result = invocable.invokeFunction("fun1", "朱晔");
        System.out.println(result);
        System.out.println(result.getClass());

        invocable.invokeFunction("fun2", new Date());
        invocable.invokeFunction("fun2", LocalDateTime.now());
        invocable.invokeFunction("fun2", new Product(1L, "aa", 1.0));
    }

    interface IA
    {
        default String getName()
        {
            return "IA";
        }
    }

    interface IB
    {
        default String getName()
        {
            return "IB";
        }
    }

    interface IC extends IA
    {
        default String getName()
        {
            return "IC";
        }
    }

    class C
    {
        public String getName()
        {
            return "C";
        }
    }

    class D implements IA
    {
    }

    class E implements IC
    {
    }

    class F implements IA, IB
    {
        @Override
        public String getName()
        {
            return IA.super.getName();
        }
    }

    class G extends C implements IA
    {

    }

    @Test
    public void defaultMethods() //默认方法,引入就是为了解决Stream的问题
    {
        assertThat(new D().getName(), is("IA")); //默认方法
        assertThat(new E().getName(), is("IC")); //默认方法的覆盖
        assertThat(new F().getName(), is("IA")); //默认方法冲突的时候手动指定
        assertThat(new G().getName(), is("C")); //子类优先继承父类的方法
    }
}
