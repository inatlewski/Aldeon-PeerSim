package org.aldeon.peersim.protocol.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PostTreeTest {

    @Test
    public void testMembership() {

        Tree t = new Tree(5);
        t.add(5, 10);
        t.add(10, 15);

        assertTrue(t.contains(5));
        assertTrue(t.contains(10));
        assertTrue(t.contains(15));
        assertFalse(t.contains(20));

    }

    @Test
    public void testAddingBranch() {

        Tree a = new Tree(2);
        a.add(2, 4);
        a.add(4, 8);

        Tree b = new Tree(3);
        b.add(3, 9);
        b.add(9, 27);

        a.add(8, b);
        assertTrue(a.contains(27));

    }

    @Test
    public void testHash() {

        Tree t = new Tree(8);
        assertEquals(t.hash(), 8);

        t.add(8, 10);
        assertEquals(t.hash(), 2);

    }

    @Test
    public void testFindById() {
        Tree t = new Tree(7);
        assertEquals(0, t.findByHash(5).size());
        t.add(7, 2);
        assertEquals(1, t.findByHash(5).size());
    }
}