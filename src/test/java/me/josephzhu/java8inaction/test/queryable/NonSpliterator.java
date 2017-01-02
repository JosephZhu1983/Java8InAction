/*
 * Copyright (c) 2016, Miguel Gamboa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.josephzhu.java8inaction.test.queryable;

import java.util.Spliterator;
import java.util.function.Consumer;

@FunctionalInterface
public interface NonSpliterator<T> extends Spliterator<T>
{

    @Override
    public abstract boolean tryAdvance(Consumer<? super T> action);

    @Override
    public default Spliterator<T> trySplit()
    {
        return null;
    }

    @Override
    public default long estimateSize()
    {
        return Long.MAX_VALUE;
    }

    @Override
    public default int characteristics()
    {
        return 0;
    }
}
