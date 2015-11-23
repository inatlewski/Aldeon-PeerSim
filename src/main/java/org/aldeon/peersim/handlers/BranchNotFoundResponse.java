package org.aldeon.peersim.handlers;

import org.aldeon.model.Tree;

import java.util.function.Consumer;


public class BranchNotFoundResponse extends Response {
    @Override
    public String toString() {
        return "BranchNotFoundResponse";
    }

    @Override
    protected void handle(Tree tree, Consumer<Request> sink) {

    }
}
