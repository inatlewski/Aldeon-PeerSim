package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.model.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Response extends Message {

    protected abstract void handle(Tree tree, Consumer<Request> sink);

    public List<Message> handle(Tree tree) {
        List<Message> requests = new ArrayList<>();
        handle(tree, requests::add);
        return requests;
    }
}