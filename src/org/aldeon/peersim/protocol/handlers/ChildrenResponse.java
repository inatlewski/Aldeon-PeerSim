package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.models.DbStub;
import org.aldeon.peersim.protocol.models.Id;
import peersim.core.Protocol;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mb on 22.06.15.
 */
public class ChildrenResponse extends AldeonMessage {
    public Map<Id, Id> children;

    public ChildrenResponse(Map<Id, Id> children) {
        this.children = children;
    }

    @Override
    public ArrayList<AldeonMessage> handle(DbStub dbStub, Protocol protocol) {
        ArrayList<AldeonMessage> result = new ArrayList<>();
        AldeonProtocol aldeonProtocol = (AldeonProtocol) protocol;

        //iterate over all children
        for (Map.Entry<Id, Id> child : children.entrySet()) {

            if (dbStub.getMessageById(child.getKey()) == null) {
                result.add(new GetBranchMessage(child.getKey()));
                continue;
            }

            //get local hash for child id
            Id localHash = dbStub.getMessageXorById(child.getKey());

            if (localHash != null && localHash.xor(child.getValue()) != Id.getEmpty()) {
                //if it's equal then it's already synchronized
                result.add(new CompareBranchMessage(child.getKey(), dbStub.getMessageXorById(child.getKey()), false));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "ChildrenResponse " + children;
    }
}
