package org.aldeon.peersim.handlers;

import org.aldeon.model.Branch;
import org.aldeon.model.Forest;

import java.util.function.Consumer;


public class BranchFoundResponse extends Response {

    private final long parent;
    private final Branch branch;

    public BranchFoundResponse(long parent, Branch branch) {
        this.parent = parent;
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "BranchFoundResponse " + branch;
    }

    @Override
    protected void handle(Forest forest, Consumer<Request> sink) {
        forest.addAll(parent, branch);
    }

}
