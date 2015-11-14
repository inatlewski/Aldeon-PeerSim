package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.models.DbStub;
import org.aldeon.peersim.protocol.models.Post;
import peersim.core.Protocol;

import java.util.ArrayList;

/**
 * Created by mb on 22.06.15.
 */
public class BranchFoundResponse extends AldeonMessage {
    public ArrayList<Post> posts;

    public BranchFoundResponse(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "BranchFoundResponse " + posts;
    }

    @Override
    public ArrayList<AldeonMessage> handle(DbStub dbStub, Protocol protocol) {
        AldeonProtocol aldeonProtocol = (AldeonProtocol) protocol;

        //TODO insert messages into database
        for (Post post : posts) {
            dbStub.insertMessage(post);
        }
        return null;
    }
}
