package org.aldeon.peersim.protocol.controls;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.vector.SingleValue;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mb on 22.06.15.
 */
public class OneMessageMissingInitializer extends BaseTreeInitializer implements Control {
    public OneMessageMissingInitializer(String prefix) {
        this.totalNumMessages = Configuration.getInt(prefix + "." + PAR_TOTAL_NUM_MSGS);
        this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
        this.seed = Configuration.getLong(PAR_RANDOM_SEED);
    }

    public boolean execute() {
        //generate a tree where every post responds to previous one
        ArrayList<Post> posts = new ArrayList<>();
        Random random = new Random(seed);
        Id previousId = Id.getEmpty(); //empty parent id
        for (int i = 0; i < totalNumMessages; i++) {
            Post newPost = new Post(new Id(random.nextLong()), previousId);
            previousId = newPost.getId();
            posts.add(newPost);
        }

        for(int i = 0; i < Network.size(); ++i) {
            AldeonProtocol aldeonProtocol = (AldeonProtocol) Network.get(i).getProtocol(this.pid);

            for (Post post : posts) {
                aldeonProtocol.dbStub.insertMessage(post);
            }
        }

        AldeonProtocol aldeonProtocol = (AldeonProtocol) Network.get(1).getProtocol(this.pid);
        aldeonProtocol.dbStub.insertMessage(new Post(new Id(random.nextLong()), previousId));

        return false;
    }
}
