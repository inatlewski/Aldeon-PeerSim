package main.java.org.aldeon.peersim.treeGenerators;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BalancedTreeGenerator extends BaseTreeGenerator {
    public static void main(String[] args) throws IOException {
        totalNumberOfPosts = 1000000;
        outputFile = "config/balancedTree1M.csv";

        CSVWriter writer = new CSVWriter(new FileWriter(outputFile));

        List<String[]> posts = new ArrayList<>();

        //add root node
        posts.add(new String[] {"1", "0"});

        //generate a balanced tree
        for (long i = 2; i <= totalNumberOfPosts; i++) {
            posts.add(new String[] {String.valueOf(i), String.valueOf(i/2)});
        }

        writer.writeAll(posts);

        writer.close();
    }
}
