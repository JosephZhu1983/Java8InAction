package me.josephzhu.java8inaction.test.collector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;

/**
 * Created by zhuye on 29/12/2016.
 */
public class BatchCollector<T> implements Collector<T, List<T>, List<T>>
{
    private final int batchSize;
    private final Consumer<List<T>> batchProcessor;

    public BatchCollector(int batchSize, Consumer<List<T>> batchProcessor)
    {
        batchProcessor = requireNonNull(batchProcessor);

        this.batchSize = batchSize;
        this.batchProcessor = batchProcessor;
    }

    public Supplier<List<T>> supplier()
    {
        return ArrayList::new;
    }

    public BiConsumer<List<T>, T> accumulator()
    {
        return (ts, t) ->
        {
            ts.add(t);
            if (ts.size() >= batchSize)
            {
                batchProcessor.accept(ts);
                ts.clear();
            }
        };
    }

    public BinaryOperator<List<T>> combiner()
    {
        return (ts, ots) ->
        {
            batchProcessor.accept(ts);
            batchProcessor.accept(ots);
            return Collections.emptyList();
        };
    }

    public Function<List<T>, List<T>> finisher()
    {
        return ts ->
        {
            batchProcessor.accept(ts);
            return Collections.emptyList();
        };
    }

    public Set<Characteristics> characteristics()
    {
        return Collections.emptySet();
    }
}