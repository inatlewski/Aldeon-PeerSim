package org.aldeon.model;

import java.util.Set;

/**
 * Branch interface. Provides basic information: identifier and children.
 * Also provides naive implementations for the basic functionality.
 */
public interface Branch {

    long ZERO = 0L;

    long identifier();
    Set<Branch> children();

    default long hash() {
        long xor = identifier();
        for (Branch branch: children()) xor = xor ^ branch.hash();
        return xor;
    }

    default boolean isRoot() {
        return identifier() == ZERO;
    }

}
