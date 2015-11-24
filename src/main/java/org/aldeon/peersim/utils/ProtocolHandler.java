package org.aldeon.peersim.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import peersim.core.Network;
import peersim.core.Node;

public abstract class ProtocolHandler {

    private static final Logger log = LoggerFactory.getLogger(ProtocolHandler.class);

    private Integer pid;
    private Integer nid;

    public void init(int pid, int nid) {
        assert this.pid == null;
        this.pid = pid;
        this.nid = nid;
    }

    protected Node node() {
        return Network.get(nid);
    }

    protected final void send(int target, Object message) {
        Packet packet = new Packet();
        packet.from = nid;
        packet.payload = message;
        Transports.send(node().getIndex(), target, pid, packet);
    }

    protected abstract void handle(int from, Object message);

    public final void processEvent(Node node, int pid2, Object event) {
        // init
        if (nid == null) init(pid2, node.getIndex());
        assert node.getIndex() == nid;
        assert this.pid == pid2;

        if (event instanceof Packet) {
            Packet packet = (Packet) event;
            handle(packet.from, packet.payload);
        } else {
            handle(-1, event);
        }
    }

    public static class Packet {
        int from;
        Object payload;
    }
}
