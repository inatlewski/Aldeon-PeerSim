package org.aldeon.treegen;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WideTreeGenerator extends BaseTreeGenerator{
    public static void main(String[] args) throws IOException {
        totalNumberOfPosts = 1000000;
        outputFile = "config/wideTree1M.csv";

        CSVWriter writer = new CSVWriter(new FileWriter(outputFile));

        List<String[]> posts = new ArrayList<>();

        //add root node
        posts.add(new String[] {"1", "0"});

        //generate a forest where every post responds to root post
        for (long i = 2; i <= totalNumberOfPosts; i++) {
            posts.add(new String[] {String.valueOf(i), "1"});
        }

        writer.writeAll(posts);

        writer.close();
    }
}
