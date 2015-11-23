package org.aldeon.peersim;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class AldeonObserver implements Control {
    /**
     * The protocol to operate on.
     */
    private static final String PAR_PROT = "protocol";

    /** Protocol identifier,*/
    private final int pid;

    public AldeonObserver(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    @Override
    public boolean execute() {
        long messagesSent = 0;
        long messagesReceived = 0;

        for (int i = 0; i < Network.size(); i++) {
            AldeonProtocol protocol = (AldeonProtocol) Network.get(i).getProtocol(pid);
            messagesSent += protocol.getMessagesSent();
            messagesReceived += protocol.getMessagesReceived();
        }

        /* Printing statistics */
        return false;
    }
}
