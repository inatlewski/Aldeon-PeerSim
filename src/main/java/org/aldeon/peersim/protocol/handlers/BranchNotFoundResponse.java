package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.model.Tree;

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
