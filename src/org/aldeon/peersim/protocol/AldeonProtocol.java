package org.aldeon.peersim.protocol;

import org.aldeon.peersim.protocol.handlers.AldeonMessage;
import org.aldeon.peersim.protocol.handlers.CompareBranchMessage;
import org.aldeon.peersim.protocol.helpers.SyncMode;
import org.aldeon.peersim.protocol.models.DbStub;
import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;
import org.aldeon.peersim.protocol.models.XorManager;
import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.vector.SingleValueHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by mb on 22.06.15.
 */
public class AldeonProtocol extends SingleValueHolder implements CDProtocol {

    public DbStub dbStub;
    private boolean initialized;
    private ArrayList<AldeonMessage> messageQueue;
    public static final String PAR_SYNC_MODE = "syncMode";
    private int syncMode;
    private long messagesSent;
    private long messagesReceived;

    public AldeonProtocol(String prefix) {
        super(prefix);

        try {
            syncMode = SyncMode.fromString(Configuration.getString(prefix + "." + PAR_SYNC_MODE));
            System.out.println("syncMode: " + SyncMode.toString(syncMode));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.dbStub = new DbStub(new XorManager());
        this.messageQueue = new ArrayList<>();
        this.initialized = false;
        this.messagesSent = 0;
        this.messagesReceived = 0;
    }

    public void putMessage(AldeonMessage message) {
        messageQueue.add(message);
    }

    public int getSyncMode() {
        return syncMode;
    }

    public long getMessagesSent() {
        return messagesSent;
    }

    public long getMessagesReceived() {
        return messagesReceived;
    }

    public void increaseMessagesSent() {
        this.messagesSent = ++messagesSent;
    }

    public void increaseMessagesReceived() {
        this.messagesReceived = ++messagesReceived;
    }

    public void printMyTree() {
        System.out.println(Id.getEmpty());
        printChildren(Id.getEmpty(), 1);
    }

    private void printChildren(Id parentId, int recursionLevel) {
        Set<Post> children = dbStub.getMessagesByParentId(parentId);
        String prompt = "";
        for (int i = 0; i < recursionLevel; i++) {
            prompt += "-";
        }
        for (Post child : children) { //children will be added in DFS order
            recursionLevel++;
            System.out.println(prompt + Long.toString(child.getId().getValue(), 36));
            printChildren(child.getId(), recursionLevel);
        }
    }

    @Override
    public void nextCycle(Node node, int protocolID) {
        int linkableID = FastConfig.getLinkable(protocolID);
        Linkable linkable = (Linkable) node.getProtocol(linkableID);

        Node peer = linkable.getNeighbor(0); //in this simulation we have only one neighbor
        if (!peer.isUp()) {
            System.out.println("peer is down");
        }

        AldeonProtocol peerAldeonProtocol = (AldeonProtocol) peer.getProtocol(protocolID);

        System.out.println("I am node " + node.getIndex()  + " num messages " + dbStub.getNumMessages());

        if (!initialized) {
            //debug - print message tree
            //printMyTree();

            initialized = true;
            if (node.getIndex() == 0) {
                //send sync request to the other party
                Id msgId = Id.getEmpty();
                Post p = dbStub.getMessageById(Id.getEmpty());
                System.out.println(p);
                Id msgXor = dbStub.getMessageXorById(Id.getEmpty());
                System.out.println("XOR for root message " + msgXor.getValue());
                peerAldeonProtocol.putMessage(new CompareBranchMessage(msgId, msgXor, false));
                this.increaseMessagesSent();
            }
        }

        //read all messages from inbox
        for (Iterator<AldeonMessage> iterator = messageQueue.iterator(); iterator.hasNext();) {
            AldeonMessage message = iterator.next();
            System.out.println("receiving message " + message + " on node " + node.getIndex());
            this.increaseMessagesReceived();
            ArrayList<AldeonMessage> responses = message.handle(dbStub, this);
            if (responses != null) {
                for (AldeonMessage response : responses) {
                    peerAldeonProtocol.putMessage(response);
                    System.out.println("sending message " + response);
                    this.increaseMessagesSent();
                }
            }
            iterator.remove();
        }
    }

    @Override
    public Object clone() { //since nodes are created by cloning, it is necessary to deep clone database stub
        AldeonProtocol aldeonProtocol = (AldeonProtocol) super.clone();
        aldeonProtocol.dbStub = new DbStub(new XorManager());
        aldeonProtocol.messageQueue = new ArrayList<>();
        return aldeonProtocol;
    }
}
