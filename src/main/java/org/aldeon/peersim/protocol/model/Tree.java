package org.aldeon.peersim.protocol.model;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tree implements Branch {

    private final Branch self;
    private final Multimap<Long, Long> children = ArrayListMultimap.create();
    private final Map<Long, Long> parents = new HashMap<>();
    private final Map<Long, Long> hashes = new HashMap<>();

    public Tree(long root) {
        parents.put(root, Branch.NONE);
        hashes.put(root, root);
        self = new View(root);
    }

    private long hash(long id) {
        Long hash = hashes.get(id);
        return hash == null ? Branch.NONE : hash;
    }

    public void add(long parent, long child) {

        if (child == Branch.NONE) throw new IllegalStateException("Cannot add: post must have an id different then zero");
        if (contains(child)) throw new IllegalStateException("Cannot add: post already present");
        if (!contains(parent)) throw new IllegalStateException("Cannot add: parent not present");

        children.put(parent, child);
        parents.put(child, parent);
        hashes.put(child, child);

        long current = parent;
        while (current != Branch.NONE) {
            hashes.put(current, hash(current) ^ child);
            current = parents.get(current);
        }
    }

    public void add(long parent, Branch child) {
        // TODO: rewrite to use queue instead of java stack
        add(parent, child.identifier());
        child.children().forEach(grandchild -> add(child.identifier(), grandchild));
    }

    public boolean contains(long id) {
        return hashes.containsKey(id);
    }

    @Override
    public long identifier() {
        return self.identifier();
    }

    @Override
    public List<Branch> children() {
        return self.children();
    }

    @Override
    public long hash() {
        return self.hash();
    }

    private Branch view(long id) {
        return new View(id);
    }

    private class View implements Branch {

        private final long id;

        private View(long id) {
            this.id = id;
        }

        @Override
        public long identifier() {
            return id;
        }

        @Override
        public List<Branch> children() {
            return Tree.this.children.get(id)
                    .stream()
                    .map(Tree.this::view)
                    .collect(Collectors.toList());
        }

        @Override
        public long hash() {
            return Tree.this.hash(id);
        }
    }

}
