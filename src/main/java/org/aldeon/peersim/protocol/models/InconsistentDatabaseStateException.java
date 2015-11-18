package org.aldeon.peersim.protocol.models;

public class InconsistentDatabaseStateException extends IllegalStateException {
    public InconsistentDatabaseStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InconsistentDatabaseStateException(String message) {
        super(message);
    }

    public InconsistentDatabaseStateException() {
        super();
    }
}
