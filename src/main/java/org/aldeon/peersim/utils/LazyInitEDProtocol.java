package org.aldeon.peersim.utils;

import peersim.core.Node;
import peersim.edsim.EDProtocol;

/**
 * Delegates protocol handling to a handler class to avoid ugly clone() process.
 */
public abstract class LazyInitEDProtocol<T extends ProtocolHandler> implements EDProtocol {

    private T handler = null;
    private final Class<T> handlerClass;

    protected LazyInitEDProtocol(Class<T> handlerClass) {
        this.handlerClass = handlerClass;
    }

    public T handler() {
        if (handler == null) {
            try {
                handler = handlerClass.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return handler;
    }

    @Override
    public void processEvent(Node node, int pid, Object event) {
        handler().processEvent(node, pid, event);
    }

    @Override
    public Object clone() {
        try {
            assert handler == null; // ensure we clone BEFORE the handlers are created
            return super.clone();
        } catch (CloneNotSupportedException e) { // should never happen
            throw new IllegalStateException(e);
        }
    }
}
