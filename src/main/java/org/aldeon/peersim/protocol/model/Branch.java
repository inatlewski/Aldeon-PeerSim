package org.aldeon.peersim.protocol.model;

import java.util.List;

/**
 * Branch interface. Provides basic information: identifier and children.
 * Also provides naive implementations for the basic functionality.
 */
public interface Branch {

    long ZERO = 0L;

    long identifier();
    long parent();
    List<Branch> children();

    default long hash() {
        long xor = identifier();
        for (Branch branch: children()) xor = xor ^ branch.hash();
        return xor;
    }

}
