package org.aldeon.peersim;

import org.aldeon.helpers.CsvTreeReader;
import org.aldeon.model.Forest;
import org.aldeon.mutator.Mutator;
import org.aldeon.peersim.utils.OneTimeControl;
import org.aldeon.peersim.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Network;
import peersim.core.Node;

public class SimulationInitializer extends OneTimeControl {

    private static final Logger log = LoggerFactory.getLogger(SimulationInitializer.class);
    private final int pid;
    private final String name;

    public SimulationInitializer(String name) {
        this.name = name;
        pid = Configuration.getPid(name + ".protocol");
        log.debug("Starting with parameter \"{}\"", name);
        log.debug("Protocol PID: {}", pid);
        log.debug("Protocol has transport? {}", FastConfig.hasTransport(pid));
    }

    @Override
    protected void initialize() throws Exception {

        // Load tree from CSV
        String file = Configuration.getString(name + ".csv");
        log.debug("Loading the tree from file {}", file);
        Forest forest = new CsvTreeReader(file).GetPostsTree();

        // Put the tree copy in each node
        Utils.forEachHandler(pid, (i, handler) -> {
            log.debug("Putting the tree in node {}", i);
            handler.getForest().addAll(forest);
        });

        // Initialize the mutator
        Class mutatorClass = Configuration.getClass(name + ".diff");
        Mutator mutator = (Mutator) mutatorClass.newInstance();
        int diffCount = Configuration.getInt(name + ".diff_count");

        // Mutate the nodes
        Utils.forEachHandler(pid, (i, handler) -> {
            if (i == 1) mutator.mutate(handler.getForest(), diffCount);
        });


        // Send the init message to start synchronization
        Utils.send(0, 0, pid, AldeonProtocolHandler.INIT_SIM);
    }

}
