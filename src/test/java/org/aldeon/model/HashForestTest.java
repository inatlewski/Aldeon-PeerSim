package org.aldeon.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashForestTest {

    @Test
    public void testContains() {
        Forest f = new HashForest();
        f.add(0, 5);
        f.add(5, 10);
        assertTrue(f.contains(5));
        assertTrue(f.contains(10));
        assertFalse(f.contains(15));
    }

    @Test
    public void testRootNeverPresent() {
        Forest f = new HashForest();
        assertFalse(f.contains(Branch.ZERO));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAddDuplicates() {
        Forest f = new HashForest();
        f.add(0, 5);
        f.add(0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotAddRoot() {
        Forest f = new HashForest();
        f.add(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentMustBePresent() {
        Forest f = new HashForest();
        f.add(5, 10);
    }

    @Test
    public void testRemove() {
        Forest f = new HashForest();
        f.add(0, 5);
        assertTrue(f.contains(5));
        assertFalse(f.remove(6));
        assertTrue(f.remove(5));
        assertFalse(f.contains(5));
    }

    @Test
    public void testHash() {
        Forest f = new HashForest();
        f.add(0, 5);
        f.add(5, 2);
        assertEquals(7, f.hash(5));
        f.add(2, 1);
        assertEquals(6, f.hash(5));
    }

}