package org.aldeon.peersim.handlers;

import org.aldeon.model.Tree;

import java.util.function.Consumer;


public class SuggestResponse extends Response {

    private final long id;
    private final long parent;
    private final long originalBranch;

    public SuggestResponse(long id, long parent, long originalBranch) {
        this.id = id;
        this.parent = parent;
        this.originalBranch = originalBranch;
    }

    @Override
    public String toString() {
        return "SuggestResponse parent=" + parent + " id=" + id;
    }

    @Override
    protected void handle(Tree tree, Consumer<Request> sink) {
        if (tree.contains(parent) && ! tree.contains(id)) {
            sink.accept(new GetBranchRequest(id));
            // then we should ask the original branch again, but we can skip it for now.
        } else {
            sink.accept(new CompareBranchRequest(originalBranch, tree.findById(originalBranch).hash(), false));
        }
    }
}
