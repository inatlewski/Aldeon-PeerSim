package org.aldeon.peersim;


import org.aldeon.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class SyncFinishedChecker implements Control {

    private static final Logger log = LoggerFactory.getLogger(SyncFinishedChecker.class);
    private final int pid;
    private int msgTotal = 0;
    private int inactive = 0;

    public SyncFinishedChecker(String name) {
        pid = Configuration.getPid(name + ".protocol");
    }

    @Override
    public boolean execute() {

        int sum = 0;
        for (int i = 0; i < Network.size(); ++i) {
            Node node = Network.get(i);
            AldeonEDProtocol protocol = (AldeonEDProtocol) node.getProtocol(pid);
            AldeonProtocolHandler handler = protocol.handler();
            sum += handler.getMsgCount();
        }

        if (sum > msgTotal) {
            msgTotal = sum;
            inactive = 0;
            return false;
        }

        inactive += 1;
        if (inactive > 10) {
            log.info("No messages sent for 10 turns, simulation concluded.");
            log.info("Messages sent in total: {}", sum);
            Simulator.result = sum;
            return true;
        }

        return false;
    }
}
