package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.model.Tree;

import java.util.List;

public abstract class Message {
    public abstract List<Message> handle(Tree tree);
}
