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

public class MessageTreeInitializer extends BaseTreeInitializer implements Control {

    private static final String PAR_SOURCE_PATH = "source";
    private String sourcePath;

    public MessageTreeInitializer(String prefix) {
        this.sourcePath = Configuration.getString(prefix + "." + PAR_SOURCE_PATH);
        this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    public boolean execute() {
        CsvTreeReader csvTreeReader = new CsvTreeReader(sourcePath);

        ArrayList<Post> loadedPosts = csvTreeReader.GetPostsTree();

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

        return false;
    }
}
