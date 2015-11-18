package org.aldeon.peersim.protocol.model;

import java.util.List;

/**
 * Branch interface. Provides basic information: identifier and children.
 * Also provides naive implementations for the basic functionality.
 */
public interface Branch {

    public static final long NONE = 0L;

    long identifier();
    List<Branch> children();

    default long hash() {
        long xor = identifier();
        for (Branch branch: children()) xor = xor ^ branch.hash();
        return xor;
    }

}
