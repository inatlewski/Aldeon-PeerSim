package org.aldeon.peersim.handlers;

import org.aldeon.model.Branch;
import org.aldeon.model.Forest;
import org.javatuples.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class ChildrenResponse extends Response {

    public final List<Pair<Long, Long>> children;

    public ChildrenResponse(List<Pair<Long, Long>> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ChildrenResponse " + children;
    }

    @Override
    protected void handle(Forest forest, Consumer<Request> sink) {

        //iterate over all children
        for (Pair<Long, Long> entry: children) {

            long id = entry.getValue0();
            long hash = entry.getValue1();

            if (forest.contains(id)) {
                long localHash = forest.hash(id);
                if (hash != localHash) sink.accept(new CompareBranchRequest(id, localHash, true));
            } else {
                sink.accept(new GetBranchRequest(id));
            }
        }
    }

    public static ChildrenResponse fromBranch(Branch branch) {
        List<Pair<Long, Long>> pairs = branch.children().stream()
                .map(b -> new Pair<>(b.identifier(), b.hash()))
                .collect(Collectors.toList());
        return new ChildrenResponse(pairs);
    }
}
