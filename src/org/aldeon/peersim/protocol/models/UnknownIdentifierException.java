package org.aldeon.peersim.protocol.models;

public class UnknownIdentifierException extends Exception {
    public UnknownIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownIdentifierException(String message) {
        super(message);
    }

    public UnknownIdentifierException() {
        super();
    }
}
