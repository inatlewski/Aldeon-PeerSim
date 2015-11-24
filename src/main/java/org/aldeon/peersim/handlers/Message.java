package org.aldeon.peersim.handlers;

import org.aldeon.model.Forest;

import java.util.List;

public abstract class Message {
    public abstract List<Message> handle(Forest forest);
}
