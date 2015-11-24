package org.aldeon.peersim.controls;

import org.aldeon.helpers.CsvTreeReader;
import org.aldeon.model.Forest;
import org.aldeon.peersim.AldeonProtocol;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import java.io.IOException;


public class GeneralTreeInitializer extends BaseTreeInitializer implements Control {

    private static final String PAR_SOURCE_PATH = "source";
    private static final String PAR_NUM_NEW = "numNewMessages"; //number of messages that should be added to the forest
    private static final String PAR_DIFFGEN = "diffGeneration";

    private static final String DIFFGEN_RANDOM_LEAF = "randomLeaf"; //new leaf in a random place
    private static final String DIFFGEN_LEAF_LEAF = "leafLeaf"; //new leaf as child of existing leaf
    private static final String DIFFGEN_RANDOM_SUBTREE = "randomSubtree";
    private static final String DIFFGEN_PROB = "probabilistic"; //probability of adding a leaf is proportional to the number of children

    private final String diffGeneration;
    private final String sourcePath;

    public GeneralTreeInitializer(String prefix) {
        this.diffGeneration = Configuration.getString(prefix + "." + PAR_DIFFGEN);
        this.sourcePath = Configuration.getString(prefix + "." + PAR_SOURCE_PATH);
        this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    public boolean execute() {
        //load forest from csv
        CsvTreeReader csvTreeReader = new CsvTreeReader(sourcePath);

        try {
            Forest loadedPosts = csvTreeReader.GetPostsTree();
            for(int i = 0; i < Network.size(); ++i) {
                AldeonProtocol aldeonProtocol = (AldeonProtocol) Network.get(i).getProtocol(this.pid);
                aldeonProtocol.forest.addAll(loadedPosts);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
