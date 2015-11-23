package org.aldeon.peersim.handlers;

import com.google.common.collect.Lists;
import org.aldeon.model.Tree;

import java.util.Collections;
import java.util.List;

public abstract class Request extends Message {
    protected abstract Response handler(Tree tree);

    @Override
    public List<Message> handle(Tree tree) {
        Response response = handler(tree);
        return response == null ? Collections.emptyList() : Lists.newArrayList(response);
    }
}
