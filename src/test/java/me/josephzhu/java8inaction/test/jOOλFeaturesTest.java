package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.model.Product;
import org.jooq.lambda.Seq;
import org.jooq.lambda.function.Function4;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple4;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by zhuye on 28/12/2016.
 */
public class jOOλFeaturesTest
{
    @Test
    public void basic()
    {
        assertArrayEquals(Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6)).toArray(), Seq.of(1, 2, 3, 4, 5, 6).toArray());

        assertTrue(Seq.of(1, 2, 3, 4).contains(2));

        assertTrue(Seq.of(1, 2, 3, 4).containsAll(2, 3));

        assertTrue(Seq.of(1, 2, 3, 4).containsAny(2, 5));

        assertArrayEquals(Seq.of(1, 2).crossJoin(Seq.of("A", "B")).toArray(),
                Seq.of(new Tuple2<>(1, "A"), new Tuple2<>(1, "B"),
                        new Tuple2<>(2, "A"), new Tuple2<>(2, "B")).toArray());

        assertArrayEquals(Seq.of(1, 2, 3).cycle().limit(8).toArray(),
                Seq.of(1, 2, 3, 1, 2, 3, 1, 2).toArray());

        assertThat(Seq.of("a", "b", "c").foldLeft("!", (u, t) -> u + t)
                , is("!abc"));

        assertThat(Seq.of("a", "b", "c").foldRight("!", (t, u) -> t + u)
                , is("abc!"));

        assertArrayEquals(Seq.of(1, 2, 3, 4).intersperse(0).toArray(), Seq.of(1,0,2,0,3,0,4).toArray());

        Seq.of(1, 2, 3, 4, 5).limitWhile(i -> i < 3);

        Seq.of(1, 2, 3, 4, 5).skipUntil(i -> i == 3);

        Seq.of(new Object(), 1, "B", 2L).ofType(Number.class);

        Seq.of(1, 2, 4).rightOuterJoin(Seq.of(1, 2, 3), (a, b) -> a == b);

        Seq.of(1, 2, 3, 4).partition(i -> i % 2 != 0);

        Seq.of(1, 2, 3, 4).remove(2);

        Seq.of(1, 2, 3, 4).removeAll(2, 3, 5);

        Seq.of(1, 2, 3, 4).retainAll(2, 3, 5);

        Seq.of(1, 2, 3, 4).reverse();

        Seq.of(1, 2, 3, 4, 5).shuffle();

        Seq.of(1, 2, 3, 4, 5).skipWhile(i -> i < 3);

        Seq.of(1, 2, 3, 4, 5).skipUntil(i -> i == 3);

        Seq.of(1, 2, 3, 4, 5).slice(1, 3);

        Seq.of(1, 2, 3, 4, 5).splitAt(2);

        Seq.of(1, 2, 3, 4, 5).splitAtHead();

        Seq.unzip(Seq.of(new Tuple2(1, "a"), new Tuple2(2, "b"), new Tuple2(3, "c")));

        Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"));

        Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (x, y) -> x + ":" + y);

        Seq.of("a", "b", "c").zipWithIndex();
    }

    @Test
    public void seqJoin()
    {
        List<Tuple2<Long, Long>> orders = new ArrayList<>();
        orders.add(new Tuple2<>(1L, 1L));
        orders.add(new Tuple2<>(2L, 111L));
        System.out.println(Seq.seq(orders.stream())
                .innerJoin(Product.getData().stream(), (a, b) -> a.v2 == b.getId()));
        System.out.println(Seq.seq(orders.stream())
                .leftOuterJoin(Product.getData().stream(), (a, b) -> a.v2 == b.getId()));
    }

    @Test
    public void extensions()
    {
        Tuple4 tuple4 = new Tuple4<>(1, 2, 3, 4);
        System.out.println(tuple4);
        Function4<Integer, Integer, Integer, Integer, Integer> function4 = (a, b, c, d) -> a + b + c + d;
        assertThat(function4.apply(tuple4), is(10));
    }
}
