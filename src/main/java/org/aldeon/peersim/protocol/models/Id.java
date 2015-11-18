package org.aldeon.peersim.protocol.models;

import java.util.Random;

/**
 Define type ID
 */
public class Id {
    private Long value;

    public boolean isEmpty() {
        if (value == null || value == 0) {
            return true;
        }
        return false;
    }


    public Id(Long value) {
        this.value = value;
    }
    public Long getValue() {
        return value;
    }

    public Id xor(Id id) {
        return new Id(value ^ id.getValue());
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public static Id getEmpty() {
        //TODO: add config parameter
        long empty = 0;
        return new Id(empty);
    }

    public static Id getRandom() {
        //TODO: add config parameter
        long seed = 12345;
        Random random = new Random(seed);
        return new Id(random.nextLong());
    }

    @Override
    public String toString() {
        return "value=" + Long.toString(value, 36);
    }

    //    public boolean equals(byte[] array2) {
//        if (value==null || array2==null)
//            return false;
//
//        int length = value.length;
//        if (array2.length != length)
//            return false;
//
//        for (int i=0; i<length; i++) {
//            byte value1 = value[i];
//            byte value2 = array2[i];
//            if (value1 != value2)
//                return false;
//        }
//
//        return true;
//    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode() ^ 11;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Id))
            return false;
        if (obj == this)
            return true;

        Id other = (Id) obj;
        return this.getValue().equals(other.getValue());
    }
}
