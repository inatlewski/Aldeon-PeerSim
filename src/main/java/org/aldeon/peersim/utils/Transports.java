package org.aldeon.peersim.utils;

import peersim.config.FastConfig;
import peersim.core.Network;
import peersim.core.Node;
import peersim.transport.Transport;

public class Transports {
    public static void send(int source, int target, int pid, Object payload) {
        Node nodeSource = Network.get(source);
        Node nodeTarget = Network.get(target);
        int tid = FastConfig.getTransport(pid);
        Transport transport = (Transport) nodeSource.getProtocol(tid);
        transport.send(nodeSource, nodeTarget, payload, pid);
    }
}
