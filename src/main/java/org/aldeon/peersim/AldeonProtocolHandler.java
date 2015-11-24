package org.aldeon.peersim;

import org.aldeon.model.Branch;
import org.aldeon.model.Forest;
import org.aldeon.model.HashForest;
import org.aldeon.peersim.handlers.CompareBranchRequest;
import org.aldeon.peersim.handlers.Message;
import org.aldeon.peersim.utils.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AldeonProtocolHandler extends ProtocolHandler {

    public static final String INIT_SIM = "init_sim";
    private static final Logger log = LoggerFactory.getLogger(AldeonProtocolHandler.class);

    private final Forest forest = new HashForest();
    private int msgCount = 0;

    @Override
    protected void handle(int from, Object event) {
        if (event instanceof Message) {
            handleMessage(from, (Message) event);
        } else if (event == INIT_SIM) {
            startSync();
        } else {
            //log.debug("Received unknown message!");
        }
    }

    private void startSync() {
        // find the root
        long root = forest.withParent(Branch.ZERO).iterator().next();
        send(1, new CompareBranchRequest(root, forest.hash(root), true));
    }

    private void handleMessage(int from, Message message) {
        msgCount += 1;
        //log.info("Node {} received message {}", 1 - from, message.getClass().getName());
        message.handle(forest).forEach(response -> send(from, response));
    }

    public Forest getForest() {
        return forest;
    }

    public int getMsgCount() {
        return msgCount;
    }
}
