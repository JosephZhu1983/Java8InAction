package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.model.Filter;
import me.josephzhu.java8inaction.test.model.FilterA;
import me.josephzhu.java8inaction.test.model.FilterB;
import org.jooq.lambda.Unchecked;
import org.junit.Test;

import java.lang.reflect.Method;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
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
    public void newLocalDate()
    {
        System.out.println(LocalDate.now());
        System.out.println(LocalDate.of(1983, 02, 17));
        System.out.println(LocalDate.of(1983, Month.FEBRUARY, 17));
        System.out.println(LocalDate.of(1983, 02, 17).plusYears(30));
        System.out.println(LocalDate.now().minus(Period.ofDays(1)));
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
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        System.out.println(LocalDateTime.now().getYear());
        System.out.println(LocalDateTime.now().getMonthValue());
        System.out.println(LocalDateTime.now().getDayOfMonth());
        System.out.println(LocalDateTime.now().getHour());
        System.out.println(LocalDateTime.now().getMinute());
        System.out.println(LocalDateTime.now().getSecond());
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

        System.out.println(DateTimeFormatter.ofPattern("yy yyyy").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("M MM MMM MMMM").withLocale(Locale.CHINESE).format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("d dd").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("e E EEEE").withLocale(Locale.CHINESE).format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("H HH").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("mm").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("ss").format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("z x").format(ZonedDateTime.now()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void optional()
    {
        assertThat(Optional.of(1).get(), is(1));
        assertThat(Optional.ofNullable(null).orElse("A"), is("A"));
        assertFalse(OptionalDouble.empty().isPresent());
        assertThat(Optional.of(1).map(Math::incrementExact).get(), is(2));
        assertThat(Optional.of(1).filter(integer -> integer % 2 == 0).orElse(null), is(nullValue()));
        Optional.empty().orElseThrow(IllegalArgumentException::new);
    }



    @Test
    public void map() //map的新增方法
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
    public void repeatableAnnotation() //可重复的注解
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
    public void checker()
    {

    }

    @Test
    public void miscImprovements()
    {

    }
}
