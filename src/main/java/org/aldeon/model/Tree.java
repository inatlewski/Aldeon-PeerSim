package org.aldeon.model;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.aldeon.utils.RevMap;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Tree implements Branch {

    private final Branch self;
    private final Multimap<Long, Long> children = ArrayListMultimap.create();
    private final Map<Long, Long> parents = new HashMap<>();
    private final RevMap<Long, Long> hashes = new RevMap<>();

    public Tree(long root) {
        parents.put(root, Branch.ZERO);
        hashes.put(root, root);
        self = new View(root, Branch.ZERO);
    }

    private long hash(long id) {
        Long hash = hashes.get(id);
        return hash == null ? Branch.ZERO : hash;
    }

    public void add(long parent, long child) {

        if (child == Branch.ZERO) throw new IllegalStateException("Cannot add: post must have an id different then zero");
        if (contains(child)) throw new IllegalStateException("Cannot add: post already present");
        if (!contains(parent)) throw new IllegalStateException("Cannot add: parent not present");

        children.put(parent, child);
        parents.put(child, parent);
        hashes.put(child, child);

        long current = parent;
        while (current != Branch.ZERO) {
            hashes.put(current, hash(current) ^ child);
            current = parents.get(current);
        }
    }

    public void add(long parent, Branch child) {
        Deque<Branch> todo = new LinkedList<>();
        todo.add(child);
        while (!todo.isEmpty()) {
            Branch current = todo.pollFirst();
            add(parent, child.identifier());
            todo.addAll(current.children());
        }
    }

    public boolean contains(long id) {
        return hashes.containsKey(id);
    }

    public int size() {
        return hashes.size();
    }

    @Override
    public long identifier() {
        return self.identifier();
    }

    @Override
    public long parent() {
        return Branch.ZERO;
    }

    @Override
    public List<Branch> children() {
        return self.children();
    }

    @Override
    public long hash() {
        return self.hash();
    }

    public Branch findById(long id) {
        return contains(id) ? new View(id, parents.get(id)) : null;
    }

    public List<Branch> findByHash(long hash) {
        return hashes.find(hash).stream().map(this::findById).collect(Collectors.toList());
    }

    public Tree copy() {
        Tree t = new Tree(identifier());
        t.children.putAll(children);
        t.parents.putAll(parents);
        t.hashes.putAll(hashes);
        return t;
    }

    /**
     * Fast tree constructor
     * @param data
     * @return
     */
    public static Tree build(List<Pair<Long, Long>> data) {

        // 1. Build the tree
        Tree tree = null;
        for (Pair<Long, Long> entry: data) {

            long parent = entry.getValue0();
            long id = entry.getValue1();

            if (tree == null) {
                assert parent == 0;
                tree = new Tree(id);
            } else {
                tree.children.put(parent, id);
                tree.parents.put(id, parent);
            }
        }

        // 2. Calculate hashes in a single pass
        if (tree != null) tree.recompute();

        return tree;
    }

    private void recompute() {
        Deque<Long> nodes = new LinkedList<>();
        Queue<Long> filler = new LinkedList<>();
        hashes.clear();

        filler.add(identifier());

        while(!filler.isEmpty()) {
            Long current = filler.poll();
            nodes.addLast(current);
            filler.addAll(children.get(current));
        }

        while (!nodes.isEmpty()) {
            Long current = nodes.pollLast();
            long sum = current;
            for (long child: children.get(current)) sum = sum ^ hashes.get(child);
            hashes.put(current, sum);
        }
    }

    private class View implements Branch {

        private final long id;
        private final long parent;

        private View(long id, long parent) {
            this.id = id;
            this.parent = parent;
        }

        @Override
        public long identifier() {
            return id;
        }

        @Override
        public long parent() {
            return parent;
        }

        @Override
        public List<Branch> children() {
            return Tree.this.children.get(id)
                    .stream()
                    .map(Tree.this::findById)
                    .collect(Collectors.toList());
        }

        @Override
        public long hash() {
            return Tree.this.hash(id);
        }
    }

}
