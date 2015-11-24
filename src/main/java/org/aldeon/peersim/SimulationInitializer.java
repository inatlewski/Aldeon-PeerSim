package org.aldeon.peersim;

import org.aldeon.helpers.CsvTreeReader;
import org.aldeon.model.Forest;
import org.aldeon.peersim.utils.OneTimeControl;
import org.aldeon.peersim.utils.Transports;
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
        for (int i = 0; i < Network.size(); ++i) {
            log.debug("Putting the tree in node {}", i);
            Node node = Network.get(i);
            AldeonEDProtocol protocol = (AldeonEDProtocol) node.getProtocol(pid);
            AldeonProtocolHandler handler = protocol.handler();
            handler.getForest().addAll(forest);
        }

        // Send the init message to first node to start synchronization
        Transports.send(0, 0, pid, AldeonProtocolHandler.INIT_SIM);
    }

}
