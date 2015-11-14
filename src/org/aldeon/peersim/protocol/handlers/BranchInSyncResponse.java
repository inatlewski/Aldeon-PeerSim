package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.models.DbStub;
import peersim.core.Protocol;

import java.util.ArrayList;

/**
 * Created by mb on 22.06.15.
 */
public class BranchInSyncResponse extends AldeonMessage {

    @Override
    public String toString() {
        return "BranchInSyncResponse";
    }

    @Override
    public ArrayList<AldeonMessage> handle(DbStub dbStub, Protocol protocol) {
        AldeonProtocol aldeonProtocol = (AldeonProtocol) protocol;
        return null;
    }
}
