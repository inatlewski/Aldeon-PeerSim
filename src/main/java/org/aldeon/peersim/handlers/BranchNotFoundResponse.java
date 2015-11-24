package org.aldeon.peersim.handlers;

import org.aldeon.model.Forest;

import java.util.function.Consumer;


public class BranchNotFoundResponse extends Response {
    @Override
    public String toString() {
        return "BranchNotFoundResponse";
    }

    @Override
    protected void handle(Forest forest, Consumer<Request> sink) {

    }
}
