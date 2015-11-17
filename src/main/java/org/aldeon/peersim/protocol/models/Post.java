package org.aldeon.peersim.protocol.models;

/**
 Define structure Post as
 id : ID
 parent : ID
 clock : long
 */
public class Post {
    private Id id;
    private Id parent;
    private Double content;

    public Post(Id id, Id parent) {
        this.id = id;
        this.parent = parent;
    }

    public Post(Id id, Id parent, Double content) {
        this.id = id;
        this.parent = parent;
        this.content = content;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Id getParent() {
        return parent;
    }

    public void setParent(Id parent) {
        this.parent = parent;
    }

    public Double getContent() {
        return content;
    }

    public void setContent(Double content) {
        this.content = content;
    }
}
