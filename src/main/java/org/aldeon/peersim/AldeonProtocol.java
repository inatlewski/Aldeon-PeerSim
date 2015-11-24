package org.aldeon.peersim;

import org.aldeon.model.Forest;
import org.aldeon.model.HashForest;
import org.aldeon.peersim.handlers.CompareBranchRequest;
import org.aldeon.peersim.handlers.Message;
import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.vector.SingleValueHolder;

import java.util.LinkedList;
import java.util.Queue;


public class AldeonProtocol extends SingleValueHolder implements CDProtocol {

    public static final long ROOT = 42;
    public Forest forest = new HashForest();
    private boolean initialized = false;
    private final Queue<Message> inbox = new LinkedList<>();
    private long messagesSent = 0;
    private long messagesReceived = 0;

    public AldeonProtocol(String prefix) {
        super(prefix);
    }

    public long getMessagesSent() {
        return messagesSent;
    }

    public long getMessagesReceived() {
        return messagesReceived;
    }


    @Override
    public void nextCycle(Node node, int protocolID) {
        int linkableID = FastConfig.getLinkable(protocolID);
        Linkable linkable = (Linkable) node.getProtocol(linkableID);
        Node peer = linkable.getNeighbor(0); // in this simulation we have only one neighbor
        if (!peer.isUp()) throw new IllegalStateException("peer is down");

        AldeonProtocol peerAldeonProtocol = (AldeonProtocol) peer.getProtocol(protocolID);
        System.out.println("I am node " + node.getIndex()  + " num messages " + forest.size());

        if (!initialized) {
            initialized = true;
            if (node.getIndex() == 0) {
                //send sync request to the other party
                long rootHash = forest.hash(ROOT);
                System.out.println("XOR for root message = " + rootHash);
                peerAldeonProtocol.inbox.add(new CompareBranchRequest(ROOT, rootHash, false));
                ++messagesSent;
            }
        }

        //read all messages from inbox
        while (true) {
            Message msg = inbox.poll();
            if (msg == null) break;

            System.out.println("receiving message " + msg + " on node " + node.getIndex());
            ++messagesReceived;

            for (Message response : msg.handle(forest)) {
                peerAldeonProtocol.inbox.add(response);
                System.out.println("sending message " + response);
                ++messagesSent;
            }
        }
    }

    @Override
    public Object clone() {
        AldeonProtocol aldeonProtocol = (AldeonProtocol) super.clone();
        aldeonProtocol.forest = new HashForest();
        aldeonProtocol.forest.addAll(forest);
        return aldeonProtocol;
    }
}
