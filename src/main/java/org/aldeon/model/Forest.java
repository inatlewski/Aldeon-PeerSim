package org.aldeon.model;

import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Interface for mutable collections of parent-child relations.
 */
public interface Forest extends Iterable<Long> {

    void add(long parent, long child);
    boolean remove(long id);
    boolean contains(long id);
    int size();
    void clear();

    long parent(long id);
    long hash(long id);
    Set<Long> withParent(long id);
    Set<Long> withHash(long hash);

    default void addAll(Forest forest) {
        for (long child: forest) add(forest.parent(child), child);
    }
    default void addAll(long parent, Branch branch) {
        Queue<Pair<Long, Branch>> queue = new LinkedList<>();
        queue.add(Pair.with(parent, branch));

        while (true) {
            Pair<Long, Branch> pair = queue.poll();
            if (pair == null) break;
            Branch current = pair.getValue1();
            add(pair.getValue0(), current.identifier());
            current.children().forEach(child -> queue.add(Pair.with(current.identifier(), child)));
        }
    }

    default Branch view(long id) {
        if (!contains(id)) return null;
        return new LambdaBranch(id, () -> hash(id), () -> withParent(id).stream().map(this::view).collect(Collectors.toSet()));
    }

}
