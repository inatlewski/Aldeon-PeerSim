package org.aldeon.peersim.protocol.controls;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import java.util.ArrayList;
import java.util.Random;

public class SingleBranchTreeInitializer extends BaseTreeInitializer  implements Control {

    public SingleBranchTreeInitializer(String prefix) {
        this.totalNumMessages = Configuration.getInt(prefix + "." + PAR_TOTAL_NUM_MSGS);
        this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
        this.seed = Configuration.getLong(PAR_RANDOM_SEED);
        this.percentOfMissingMessages = Configuration.getInt(PAR_PERCENT_OF_MISSING_MESSAGES);
    }

    public boolean execute() {
        Random random = new Random(seed);
        Id previousId = Id.getEmpty(); //empty parent id

        //generate a tree where every post responds to previous one
        ArrayList<Post> posts = new ArrayList<>();

        for (int i = 0; i < totalNumMessages; i++) {
            Post newPost = new Post(new Id(random.nextLong()), previousId);
            previousId = newPost.getId();
            posts.add(newPost);
        }

        int initialMessageNumber = (100 - percentOfMissingMessages) * totalNumMessages / 100;
        AldeonProtocol destinationNode = (AldeonProtocol) Network.get(0).getProtocol(this.pid);

        for (int i = 0; i < initialMessageNumber; i++) {
            destinationNode.dbStub.insertMessage(posts.get(i));
        }

        AldeonProtocol sourceNode = (AldeonProtocol) Network.get(1).getProtocol(this.pid);

        for (Post post : posts) {
            sourceNode.dbStub.insertMessage(post);
        }

        return false;
    }
}
