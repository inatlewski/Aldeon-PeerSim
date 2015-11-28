package org.aldeon.mutator;

import org.aldeon.model.Forest;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by mb on 28.11.15.
 */
public class LeafIteratingMutator implements Mutator {
    @Override
    public void mutate(Forest forest, int diffs) {
        Random r = new Random();

        List<Long> leaves = forest.stream()
                .filter(n -> forest.withParent(n).isEmpty())
                .collect(Collectors.toList());

        for (int i = 0; i < diffs; ++i) {
            long parent = leaves.get(r.nextInt(leaves.size()));
            long child = r.nextLong();
            forest.add(parent, child);
            leaves.remove(parent);
            leaves.add(child);
        }
    }
}
