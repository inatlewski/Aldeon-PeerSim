package org.aldeon.peersim.handlers;

import org.aldeon.model.Branch;
import org.aldeon.model.Tree;

import java.util.function.Consumer;


public class BranchFoundResponse extends Response {

    private final Branch branch;

    public BranchFoundResponse(Branch branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "BranchFoundResponse " + branch;
    }

    @Override
    protected void handle(Tree tree, Consumer<Request> sink) {
        tree.add(branch.parent(), branch);
    }

}
