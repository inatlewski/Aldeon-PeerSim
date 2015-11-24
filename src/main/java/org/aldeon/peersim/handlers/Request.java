package org.aldeon.peersim.handlers;

import com.google.common.collect.Lists;
import org.aldeon.model.Forest;

import java.util.Collections;
import java.util.List;

public abstract class Request extends Message {
    protected abstract Response handler(Forest forest);

    @Override
    public List<Message> handle(Forest forest) {
        Response response = handler(forest);
        return response == null ? Collections.emptyList() : Lists.newArrayList(response);
    }
}
