package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.models.DbStub;
import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;
import peersim.core.Protocol;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by mb on 22.06.15.
 */
public class GetBranchMessage extends AldeonMessage {
    private Id id;

    /**
     * @param id - id of desired branch
     */
    public GetBranchMessage(Id id) {
        this.id = id;
    }

    @Override
    public ArrayList<AldeonMessage> handle(DbStub dbStub, Protocol protocol) {
        AldeonProtocol aldeonProtocol = (AldeonProtocol) protocol;
        aldeonProtocol.increaseMessagesReceived();

        ArrayList<AldeonMessage> result = new ArrayList<>();
        ArrayList<Post> branchPosts = new ArrayList<>();

        if (dbStub.getMessageById(id) != null) {
            branchPosts.add(dbStub.getMessageById(id));
            recGetChildren(id, branchPosts, dbStub);
            result.add(new BranchFoundResponse(branchPosts));
            aldeonProtocol.increaseMessagesSent();
        } else {
            result.add(new BranchNotFoundResponse());
            aldeonProtocol.increaseMessagesSent();
        }

        return result;
    }

    private void recGetChildren(Id parentId, ArrayList<Post> accList, DbStub dbStub) {
        Set<Post> children = dbStub.getMessagesByParentId(parentId);
        for (Post child : children) { //children will be added in DFS order
            accList.add(child);
            recGetChildren(child.getId(), accList, dbStub);
        }
    }

    @Override
    public String toString() {
        return "GetBranchMessage branch id = " + id;
    }
}
