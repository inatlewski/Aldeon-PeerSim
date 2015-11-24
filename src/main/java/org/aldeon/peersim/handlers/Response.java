package org.aldeon.peersim.handlers;

import org.aldeon.model.Forest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Response extends Message {

    protected abstract void handle(Forest forest, Consumer<Request> sink);

    public List<Message> handle(Forest forest) {
        List<Message> requests = new ArrayList<>();
        handle(forest, requests::add);
        return requests;
    }
}
