package me.josephzhu.java8inaction.test.queryable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by zhuye on 02/01/2017.
 */

@FunctionalInterface
public interface Queryable<T> extends NonSpliterator<T>
{
    static <T> boolean truth(Consumer<T> c, T item)
    {
        c.accept(item);
        return true;
    }

    static <T> boolean consumeNext(Queryable<T> query, Predicate<T> pred, Consumer<? super T> action)
    {
        final boolean[] found = {false};
        while (!found[0] &&
                query.tryAdvance(e ->
                {
                    if (pred.test(e))
                    {
                        action.accept(e);
                        found[0] = true;
                    }
                })) ;
        return found[0];
    }

    static <T> Queryable<T> of(Collection<T> data)
    {
        final Iterator<T> dataSrc = data.iterator();
        return action -> dataSrc.hasNext() ? truth(action, dataSrc.next()) : false;
    }

    default void forEach(Consumer<T> action)
    {
        while (tryAdvance(action)) ;
    }

    default <R> Queryable<R> map(Function<T, R> mapper)
    {
        return action -> tryAdvance(item -> action.accept(mapper.apply(item)));
    }

    default Queryable<T> limit(long maxSize)
    {
        final int[] count = {0};
        return action -> count[0]++ < maxSize ? tryAdvance(action) : false;
    }

    default Queryable<T> distinct()
    {
        final Set<T> selected = new HashSet<>();
        return action -> consumeNext(this, selected::add, action);
    }

    default Queryable<T> filter(Predicate<T> p)
    {
        return action -> consumeNext(this, p, action);
    }

    default T reduce(T initial, BinaryOperator<T> accumulator)
    {
        final T[] res = (T[]) Array.newInstance(initial.getClass(), 1);
        res[0] = initial;
        forEach(e -> res[0] = accumulator.apply(res[0], e));
        return res[0];
    }

    default List<T> toList()
    {
        List<T> res = new ArrayList<>();
        forEach(e -> res.add(e));
        return res;
    }
}