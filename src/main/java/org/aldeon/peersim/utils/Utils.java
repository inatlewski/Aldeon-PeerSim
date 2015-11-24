package org.aldeon.peersim.utils;

import org.aldeon.peersim.AldeonEDProtocol;
import org.aldeon.peersim.AldeonProtocolHandler;
import peersim.config.FastConfig;
import peersim.core.Network;
import peersim.core.Node;
import peersim.transport.Transport;

import java.util.function.BiConsumer;

public class Utils {
    public static void send(int source, int target, int pid, Object payload) {
        Node nodeSource = Network.get(source);
        Node nodeTarget = Network.get(target);
        int tid = FastConfig.getTransport(pid);
        Transport transport = (Transport) nodeSource.getProtocol(tid);
        transport.send(nodeSource, nodeTarget, payload, pid);
    }

    public static void forEachHandler(int pid, BiConsumer<Integer, AldeonProtocolHandler> consumer) {
        for (int i = 0; i < Network.size(); ++i) {
            Node node = Network.get(i);
            AldeonEDProtocol protocol = (AldeonEDProtocol) node.getProtocol(pid);
            AldeonProtocolHandler handler = protocol.handler();
            consumer.accept(i, handler);
        }
    }
}
