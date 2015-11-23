package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.model.Branch;
import org.aldeon.peersim.protocol.model.Tree;
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
    protected void handle(Tree tree, Consumer<Request> sink) {

        //iterate over all children
        for (Pair<Long, Long> entry: children) {

            long id = entry.getValue0();
            long hash = entry.getValue1();
            Branch local = tree.findById(id);

            if (local == null)
                sink.accept(new GetBranchRequest(id));
            else if (hash != local.hash())
                sink.accept(new CompareBranchRequest(id, local.hash(), false));
        }
    }

    public static ChildrenResponse fromBranch(Branch branch) {
        List<Pair<Long, Long>> pairs = branch.children().stream()
                .map(b -> new Pair<>(b.identifier(), b.hash()))
                .collect(Collectors.toList());
        return new ChildrenResponse(pairs);
    }
}
