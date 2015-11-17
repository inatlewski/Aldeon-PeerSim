package org.aldeon.peersim.treeGenerators;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleBranchGenerator extends BaseTreeGenerator {

    public static void main(String [ ] args) throws IOException {

        totalNumberOfPosts = 1000000;
        outputFile = "config/singleBranch1M.csv";

        CSVWriter writer = new CSVWriter(new FileWriter(outputFile));

        //generate a tree where every post responds to previous one
        List<String[]> posts = new ArrayList<>();

        for (long i = 1; i <= totalNumberOfPosts; i++) {
            posts.add(new String[] {String.valueOf(i), String.valueOf(i - 1)});
        }

        writer.writeAll(posts);

        writer.close();
    }
}

