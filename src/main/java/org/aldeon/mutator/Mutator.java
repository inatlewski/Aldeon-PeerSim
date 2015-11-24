package org.aldeon.mutator;

import org.aldeon.model.Forest;

public interface Mutator {
    void mutate(Forest forest, int diffs);
}
