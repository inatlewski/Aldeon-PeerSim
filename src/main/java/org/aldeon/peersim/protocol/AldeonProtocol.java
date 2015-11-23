package org.aldeon.peersim.protocol;

import org.aldeon.peersim.protocol.handlers.CompareBranchRequest;
import org.aldeon.peersim.protocol.handlers.Message;
import org.aldeon.peersim.protocol.model.Tree;
import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.vector.SingleValueHolder;
import java.util.LinkedList;
import java.util.Queue;


public class AldeonProtocol extends SingleValueHolder implements CDProtocol {

    public static final long ROOT = 42;
    public Tree tree = new Tree(ROOT);
    private boolean initialized = false;
    private final Queue<Message> inbox = new LinkedList<>();
    private long messagesSent = 0;
    private long messagesReceived = 0;

    public AldeonProtocol(String prefix) {
        super(prefix);
    }

    public void setTree(Tree tree) {
        this.tree = tree;
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
        System.out.println("I am node " + node.getIndex()  + " num messages " + tree.size());

        if (!initialized) {
            initialized = true;
            if (node.getIndex() == 0) {
                //send sync request to the other party
                long rootHash = tree.findById(ROOT).hash();
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

            for (Message response : msg.handle(tree)) {
                peerAldeonProtocol.inbox.add(response);
                System.out.println("sending message " + response);
                ++messagesSent;
            }
        }
    }

    @Override
    public Object clone() { //since nodes are created by cloning, it is necessary to deep clone database stub
        AldeonProtocol aldeonProtocol = (AldeonProtocol) super.clone();
        aldeonProtocol.tree = tree.copy();
        return aldeonProtocol;
    }
}
