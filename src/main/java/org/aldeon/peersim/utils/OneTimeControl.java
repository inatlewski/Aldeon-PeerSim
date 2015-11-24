package org.aldeon.peersim.utils;

import peersim.core.Control;

public abstract class OneTimeControl implements Control {

    private boolean done = false;

    @Override
    public final boolean execute() {
        if (done) return false;
        try {
            initialize();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        done = true;
        return false;
    }

    protected abstract void initialize() throws Exception;
}
