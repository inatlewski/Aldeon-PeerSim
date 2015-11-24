package org.aldeon.model;

import java.util.Set;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class LambdaBranch implements Branch {

    private final long id;
    private final LongSupplier hashSupplier;
    private final Supplier<Set<Branch>> childrenSupplier;

    public LambdaBranch(long id, LongSupplier hashSupplier, Supplier<Set<Branch>> childrenSupplier) {
        this.id = id;
        this.hashSupplier = hashSupplier;
        this.childrenSupplier = childrenSupplier;
    }

    @Override
    public long identifier() {
        return id;
    }

    @Override
    public long hash() {
        return hashSupplier.getAsLong();
    }

    @Override
    public Set<Branch> children() {
        return childrenSupplier.get();
    }
}
