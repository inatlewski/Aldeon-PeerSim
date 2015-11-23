package org.aldeon.peersim.protocol.utils;

public class Nester {
    private static final String UNIT_TAB = "  ";
    private final StringBuilder b;
    private final String tab;

    public Nester() {
        b = new StringBuilder();
        tab = "";
    }

    private Nester(StringBuilder b, String tab) {
        this.b = b;
        this.tab = tab;
    }

    public void add(String string) {
        String tabbed = string.replace("\n", "\n" + tab);
        b.append(tabbed);
    }

    public Nester sub() {
        return new Nester(b, tab + UNIT_TAB);
    }

    public int indent() {
        return tab.length();
    }

    @Override
    public String toString() {
        return b.toString();
    }
}
