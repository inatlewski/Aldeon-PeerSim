package org.aldeon.peersim.protocol.controls;

import org.aldeon.peersim.protocol.AldeonProtocol;
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

public class CsvTreeInitializer extends BaseTreeInitializer implements Control {
    private static final String PAR_SOURCE_PATH = "source";
    private static final String PAR_MAX_TIMESTAMP = "maxTimestamp";

    private String sourcePath;
    private Double maxTimestamp;

    public CsvTreeInitializer(String prefix) {
        this.sourcePath = Configuration.getString(prefix + "." + PAR_SOURCE_PATH);
        this.maxTimestamp = Double.parseDouble(Configuration.getString(prefix + "." + PAR_MAX_TIMESTAMP));
        this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    public boolean execute() {
        ArrayList<Post> posts = new ArrayList<>();
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";
        String csvQuoteChar = "\"";

        System.out.println("CSV source file path: " + sourcePath);

        try {
            br = new BufferedReader(new FileReader(sourcePath));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);

                if(values.length != 3)
                    break;

                String postFullId = values[0].replaceAll(csvQuoteChar, "");
                String parentFullId = values[1].replaceAll(csvQuoteChar, "");
                String postTimestamp = values[2].replaceAll(csvQuoteChar, "");

                double numericTimestamp = Double.parseDouble(postTimestamp);

                String[] postFullIdSplit = postFullId.split("_");
                String[] parentFullIdSplit = parentFullId.split("_");

                long postId = Long.parseLong(postFullIdSplit[1], 36);

                long parentId = Long.parseLong(parentFullIdSplit[1], 36);
                if (parentFullIdSplit[0].equals("t3")) {
                    parentId = 0;
                }

                Post newPost = new Post(new Id(postId), new Id(parentId), numericTimestamp);

                posts.add(newPost);
            }

            System.out.println("Comments loaded from CSV file: " + posts.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Collections.sort(posts, new Comparator<Post>() {
            public int compare(Post one, Post other) {
                return one.getContent().compareTo(other.getContent());
            }
        });

        int insertedPostCount;

        AldeonProtocol aldeonProtocol = (AldeonProtocol) Network.get(0).getProtocol(this.pid);

        insertedPostCount = 0;
        for (Post post : posts) {
            if (post.getContent().compareTo(maxTimestamp) > 0) {
                continue;
            }

            DbStub.PutStatus putStatus = aldeonProtocol.dbStub.insertMessage(post);
            if (putStatus == DbStub.PutStatus.SUCCESS) {
                insertedPostCount++;
            }
            else {
                return false;
            }
        }

        System.out.println("Comments inserted to node 0 db stub: " + insertedPostCount);

        AldeonProtocol fullNodeAldeonProtocol = (AldeonProtocol) Network.get(1).getProtocol(this.pid);

        insertedPostCount = 0;
        for (Post post : posts) {
            DbStub.PutStatus putStatus = fullNodeAldeonProtocol.dbStub.insertMessage(post);
            if (putStatus == DbStub.PutStatus.SUCCESS)
                insertedPostCount++;
        }

        System.out.println("Comments inserted to node 1 db stub: " + insertedPostCount);

        return false;
    }
}
