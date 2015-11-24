package org.aldeon.mutator;

import com.google.common.collect.Lists;
import org.aldeon.model.Forest;

import java.util.List;
import java.util.Random;

public class UniformMutator implements Mutator {

    @Override
    public void mutate(Forest forest, int diffs) {
        List<Long> nodes = Lists.newArrayList(forest);

        Random r = new Random();
        for (int i = 0; i < diffs; ++i) {
            long parent = nodes.get(r.nextInt(nodes.size()));
            long child = r.nextLong();
            forest.add(parent, child);
        }
    }
}
