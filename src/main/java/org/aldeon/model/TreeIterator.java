package org.aldeon.model;

import java.util.*;
import java.util.function.Function;

public class TreeIterator<T> implements Iterator<T> {

    private final Queue<T> queue = new LinkedList<>();
    private final Function<T, Collection<? extends T>> childFunction;

    public TreeIterator(Collection<? extends T> initial, Function<T, Collection<? extends T>> childFunction) {
        this.childFunction = childFunction;
        queue.addAll(initial);
    }

    @Override
    public boolean hasNext() {
        return ! queue.isEmpty();
    }

    @Override
    public T next() {
        T next = queue.remove();
        queue.addAll(childFunction.apply(next));
        return next;
    }
}
