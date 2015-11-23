package org.aldeon.peersim.handlers;

import org.aldeon.model.Tree;

import java.util.List;

public abstract class Message {
    public abstract List<Message> handle(Tree tree);
}
