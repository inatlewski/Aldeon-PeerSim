package org.aldeon.peersim.protocol.controls;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.helpers.CsvTreeReader;
import org.aldeon.peersim.protocol.models.DbStub;
import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class GeneralTreeInitializer extends BaseTreeInitializer implements Control {

    private static final String PAR_SOURCE_PATH = "source";
    private static final String PAR_NUM_NEW = "numNewMessages"; //number of messages that should be added to the tree
    private static final String PAR_DIFFGEN = "diffGeneration";

    private static final String DIFFGEN_RANDOM_LEAF = "randomLeaf"; //new leaf in a random place
    private static final String DIFFGEN_LEAF_LEAF = "leafLeaf"; //new leaf as child of existing leaf
    private static final String DIFFGEN_RANDOM_SUBTREE = "randomSubtree";
    private static final String DIFFGEN_PROB = "probabilistic"; //probability of adding a leaf is proportional to the number of children

    private String diffGeneration;
    private String sourcePath;
    private Integer numNewMessages;

    public GeneralTreeInitializer(String prefix) {
        this.diffGeneration = Configuration.getString(prefix + "." + PAR_DIFFGEN);
        this.numNewMessages = Integer.parseInt(Configuration.getString(prefix + "." + PAR_NUM_NEW));
        this.sourcePath = Configuration.getString(prefix + "." + PAR_SOURCE_PATH);
        this.pid = Configuration.getPid(prefix + "." + PAR_PROT);

        System.out.println("num new messages " + numNewMessages);
    }

    public boolean execute() {
        //load tree from csv
        CsvTreeReader csvTreeReader = new CsvTreeReader(sourcePath);

        ArrayList<Post> loadedPosts = null;

        try {
            loadedPosts = csvTreeReader.GetPostsTree();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int insertedPostCount;

        for(int i = 0; i < Network.size(); ++i) {
            insertedPostCount = 0;

            AldeonProtocol aldeonProtocol = (AldeonProtocol) Network.get(i).getProtocol(this.pid);

            for (Post post : loadedPosts) {
                DbStub.PutStatus putStatus = aldeonProtocol.dbStub.insertMessage(post);
                if (putStatus == DbStub.PutStatus.SUCCESS)
                    insertedPostCount++;
            }

            System.out.println("Posts inserted to node " + i + " db stub: " + insertedPostCount);
        }

        AldeonProtocol fullNodeProtocol = (AldeonProtocol) Network.get(1).getProtocol(this.pid);

        //TODO generate differences
        switch (this.diffGeneration) {
            case DIFFGEN_RANDOM_LEAF:
                System.out.println("using random leaf generator"); //param: number of messages to add

                break;
            case DIFFGEN_LEAF_LEAF:
                System.out.println("using leaf leaf generator");

                break;
            case DIFFGEN_RANDOM_SUBTREE:
                System.out.println("using random subtree generator");

                break;
            case DIFFGEN_PROB:
                System.out.println("using probabilistic generator");

                break;
        }

        return false;
    }
}