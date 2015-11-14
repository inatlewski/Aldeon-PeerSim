package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.helpers.SyncMode;
import org.aldeon.peersim.protocol.models.DbStub;
import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;
import peersim.core.Protocol;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by mb on 22.06.15.
 */
public class CompareBranchMessage extends AldeonMessage {

    private Id Ib;
    private Id Sa;
    private boolean f;

    /**
     * @param Ib - branch root identifier
     * @param Sa - value of branch hash function of the other node
     * @param f - force, if set to true, the suggest option is disabled
     */
    public CompareBranchMessage(Id Ib, Id Sa, boolean f) {
        this.Ib = Ib;
        this.Sa = Sa;
        this.f = f;
    }

    @Override
    public String toString() {
        return "CompareBranchMessage Ib=" + Ib + ", Sa=" + Sa + ", f=" + f;
    }

    @Override
    public ArrayList<AldeonMessage> handle(DbStub dbStub, Protocol protocol) {
        ArrayList<AldeonMessage> results = new ArrayList<>();
        Post post = dbStub.getMessageById(Ib);
        AldeonProtocol aldeonProtocol = (AldeonProtocol) protocol;

        if (post == null && !this.Ib.equals(Id.getEmpty())) {
            results.add(new BranchNotFoundResponse());
            return results;
        }

        Id diff = Sa.xor(dbStub.getMessageXorById(Ib));

        if (!f && aldeonProtocol.getSyncMode() == SyncMode.BRANCH_HASH_WITH_SUGGEST) {
            //check if there is a branch matching diff

            Set<Id> matchingBranch = dbStub.getMessageIdsByXor(diff);
            if (!matchingBranch.isEmpty()) {
                for (Id id : matchingBranch) {
                    results.add(new SuggestResponse(id, dbStub.getMessageById(id).getParent(), Ib));
                    return results;
                }
            }
        }

        if (diff.equals(Id.getEmpty())) {
            results.add(new BranchInSyncResponse());
            return results;
        } else { //return children
            results.add(new ChildrenResponse(dbStub.getIdsAndXorsByParentId(Ib)));
            return results;

        }
        //check if Ib belongs to our tree
        //if it doesn't belong then return branchNotFound
    }
}
