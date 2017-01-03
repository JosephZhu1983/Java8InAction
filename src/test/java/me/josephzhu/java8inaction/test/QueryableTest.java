package me.josephzhu.java8inaction.test;

import me.josephzhu.java8inaction.test.queryable.Queryable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhuye on 02/01/2017.
 */
public class QueryableTest
{
    private final List<String> data = Arrays
            .asList("aa", "bb", "-cc", "dd", "-ee", "aa", "bb");

    @Test
    public void testQueryableForEach()
    {

        Iterator<String> expected = Arrays
                .asList("aa", "bb", "-cc", "dd", "-ee", "aa", "bb")
                .iterator();
        Queryable
                .of(data)
                .forEach(w -> Assert.assertEquals(expected.next(), w));

    }

    @Test
    public void testQueryableMap()
    {

        final Integer[] expected = {2, 2, 3, 2, 3, 2, 2};
        final List<Integer> actual = Queryable
                .of(data)
                .map(String::length)
                .toList();
        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testQueryableFilter()
    {
        final String[] expected = {"-cc", "-ee"};
        final List<String> actual = Queryable
                .of(data)
                .filter(w -> w.startsWith("-"))
                .toList();
        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testQueryableDistinct()
    {
        final String[] expected = {"aa", "bb", "-cc", "dd", "-ee"};
        final List<String> actual = Queryable
                .of(data)
                .distinct()
                .toList();
        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testQueryableLimit()
    {
        final String[] expected = {"aa", "bb", "-cc"};
        final List<String> actual = Queryable
                .of(data)
                .limit(3)
                .toList();
        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void testQueryableReduce()
    {
        String actual = Queryable
                .of(data)
                .reduce("", String::concat);
        Assert.assertEquals(actual, "aabb-ccdd-eeaabb");
    }

    @Test
    public void testQueryableChain()
    {
        final Iterator<Integer> expected = Arrays.asList(2, 2, 2).iterator();

        Queryable
                .of(data)
                .distinct()
                .filter(w -> !w.startsWith("-"))
                .map(String::length)
                .limit(3)
                .forEach(l -> Assert.assertEquals(expected.next(), l));
    }
}
