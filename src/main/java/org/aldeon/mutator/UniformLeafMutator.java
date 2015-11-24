package org.aldeon.mutator;

import org.aldeon.model.Forest;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UniformLeafMutator implements Mutator {
    @Override
    public void mutate(Forest forest, int diffs) {
        List<Long> leaves = forest.stream()
                .filter(n -> forest.withParent(n).isEmpty())
                .collect(Collectors.toList());

        Random r = new Random();
        for (int i = 0; i < diffs; ++i) {
            long parent = leaves.get(r.nextInt(leaves.size()));
            long child = r.nextLong();
            forest.add(parent, child);
        }
    }
}
