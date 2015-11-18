package org.aldeon.peersim.protocol.controls;

import org.aldeon.peersim.protocol.AldeonProtocol;
import org.aldeon.peersim.protocol.helpers.CsvTreeReader;
import org.aldeon.peersim.protocol.model.Tree;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

import java.io.IOException;


public class MessageTreeInitializer extends BaseTreeInitializer implements Control {

    private static final String PAR_SOURCE_PATH = "source";
    private String sourcePath;

    public MessageTreeInitializer(String prefix) {
        this.sourcePath = Configuration.getString(prefix + "." + PAR_SOURCE_PATH);
        this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    public boolean execute() {
        CsvTreeReader csvTreeReader = new CsvTreeReader(sourcePath);

        try {
            Tree loadedPosts = csvTreeReader.GetPostsTree();
            for(int i = 0; i < Network.size(); ++i) {
                AldeonProtocol aldeonProtocol = (AldeonProtocol) Network.get(i).getProtocol(this.pid);
                aldeonProtocol.setTree(loadedPosts.copy());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        return false;
    }
}
