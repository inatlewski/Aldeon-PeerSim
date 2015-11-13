package org.aldeon.peersim.protocol.models;

/**
 Define type Address
 */
public class Address {
    private byte[] value;

    public Address(byte[] array) {
        value = array;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
