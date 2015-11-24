package org.aldeon.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.aldeon.utils.RevMap;

import java.util.*;
import java.util.stream.Collectors;

public class HashForest implements Forest {

    private final Multimap<Long, Long> children = ArrayListMultimap.create();
    private final Map<Long, Long> parents = new HashMap<>();
    private final RevMap<Long, Long> hashes = new RevMap<>();

    @Override
    public void add(long parent, long child) {
        if (child == Branch.ZERO) throw new IllegalArgumentException("Post identifier can not be equal to zero");
        if (contains(child)) throw new IllegalArgumentException("Post already present");
        if (parent != 0 && !contains(parent)) throw new IllegalArgumentException("Parent not present");

        children.put(parent, child);
        parents.put(child, parent);
        invalidate(child);
    }

    @Override
    public boolean remove(long id) {
        if (! contains(id)) return false;
        invalidate(id);
        long parent = parents.remove(id);
        children.remove(parent, id);
        return true;
    }

    @Override
    public boolean contains(long id) {
        return parents.containsKey(id);
    }

    @Override
    public int size() {
        return parents.size();
    }

    @Override
    public void clear() {
        children.clear();
        parents.clear();
        hashes.clear();
    }

    @Override
    public long hash(long id) {
        Long hash = hashes.get(id);
        if (hash == null) {
            recompute(id);
            hash = hashes.get(id);
        }
        return hash;
    }

    @Override
    public long parent(long id) {
        return parents.get(id);
    }

    @Override
    public Set<Long> withParent(long id) {
        return new HashSet<>(children.get(id));
    }

    @Override
    public Set<Long> withHash(long hash) {
        return hashes.find(hash);
    }

    @Override
    public Iterator<Long> iterator() {
        return new TreeIterator<>(withParent(Branch.ZERO), this::withParent);
    }

    ////////////////////////////////////////////////////////////////////

    private void recompute(long id) {
        List<Long> list = new ArrayList<>();
        list.add(id);
        for(int i = 0; i < list.size(); ++i) {
            list.addAll(
                    children.get(list.get(i))
                    .stream()
                    .filter(child -> !hashes.containsKey(child))
                    .collect(Collectors.toList())
            );
        }
        for(int i = list.size() - 1; i >= 0; --i) {
            long current = list.get(i);
            long sum = current;
            for (long child: children.get(current)) {
                sum = sum ^ hashes.get(child);
            }
            hashes.put(current, sum);
        }
    }

    private void invalidate(long id) {
        hashes.remove(id);
        while (true) {
            id = parents.get(id);
            if (hashes.remove(id) == null) break;
        }
    }
}
