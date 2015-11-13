package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.models.DbStub;
import peersim.core.Protocol;

import java.util.ArrayList;

/**
 * Created by mb on 22.06.15.
 */
public class BranchNotFoundResponse extends AldeonMessage {
    @Override
    public String toString() {
        return "BranchNotFoundResponse";
    }

    @Override
    public ArrayList<AldeonMessage> handle(DbStub dbStub, Protocol protocol) {
        AldeonProtocol aldeonProtocol = (AldeonProtocol) protocol;
        aldeonProtocol.increaseMessagesReceived();
        return null;
    }
}
