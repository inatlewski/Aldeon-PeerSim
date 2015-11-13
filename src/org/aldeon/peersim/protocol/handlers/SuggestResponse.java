package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.models.DbStub;
import org.aldeon.peersim.protocol.models.Id;
import peersim.core.Protocol;

import java.util.ArrayList;

/**
 * Created by mb on 22.06.15.
 */
public class SuggestResponse extends AldeonMessage {
    private Id parentId;
    private Id Ig;
    //TODO we need Ib - send in message <- differs from article
    private Id Ib;

    public SuggestResponse(Id Ig, Id parentId, Id Ib) {
        this.parentId = parentId;
        this.Ig = Ig;
        this.Ib = Ib;
    }

    @Override
    public ArrayList<AldeonMessage> handle(DbStub dbStub, Protocol protocol) {
        AldeonProtocol aldeonProtocol = (AldeonProtocol) protocol;
        aldeonProtocol.increaseMessagesReceived();

        ArrayList<AldeonMessage> result = new ArrayList<>();

        //TODO check for bugs

        System.out.println("get suggest " + dbStub.getMessageById(Ig));
        System.out.println("check ancestry " + dbStub.checkAncestry(parentId, Ib));

        if (dbStub.getMessageById(Ig) == null && dbStub.checkAncestry(parentId, Ib)) {
            //we don't have Ig
            result.add(new GetBranchMessage(Ig));
            aldeonProtocol.increaseMessagesSent();

            //we have Ib
            result.add(new CompareBranchMessage(Ib, dbStub.getMessageXorById(Ib), false));
            aldeonProtocol.increaseMessagesSent();
        } else {
            result.add(new CompareBranchMessage(Ib, dbStub.getMessageXorById(Ib), true));
            aldeonProtocol.increaseMessagesSent();
        }

        return result;
    }

    @Override
    public String toString() {
        return "SuggestResponse parentId=" + parentId + " Ig=" + Ig + " Ib=" + Ib;
    }
}
