package org.aldeon.peersim.treeGenerators;

import com.opencsv.CSVWriter;
import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by mb on 18.11.15.
 */
public class FurryListGenerator extends BaseTreeGenerator {

    public static void main(String[] args) throws IOException {
        totalNumberOfPosts = 1000000; //this works cleanly for multiples of 10
        outputFile = "config/furryList1M.csv";

        double hairDensity = 0.5; //one in two messages has a strand of hair attached

        //totalNum == numOnList * hairDensity + numOnList
        //totalNum == numOnList * (hairDensity+ 1)
        //totalNum / (hairDensity + 1) == numOnList

        int numOnList = (int) (totalNumberOfPosts / (hairDensity + 1.0));
        int numStrands = (int) (numOnList * hairDensity);
        numOnList = totalNumberOfPosts - numStrands;

        //pick numStrands values from range 1..numOnList
        Integer randomizer[] = new Integer[numOnList];
        for (int i = 0; i < numOnList; i++) {
            randomizer[i] = i+1;
        }

        Collections.shuffle(Arrays.asList(randomizer));
        Integer[] strandsIndices = new Integer[numStrands];
        System.arraycopy(randomizer, 0, strandsIndices, 0, numStrands);
        Arrays.sort(strandsIndices);
//        System.out.println(Arrays.toString(strandsIndices));

        List<String[]> posts = new ArrayList<>();

        //generate a tree where every post responds to previous one
        for (long i = 1; i <= numOnList; i++) {
            posts.add(new String[] {String.valueOf(i), String.valueOf(i - 1)});
        }

        //add furrrrrrr
        for (int i = 1; i <= numStrands; i++) {
            posts.add(new String[] {String.valueOf(i+numOnList), String.valueOf(strandsIndices[i-1])});

        }

        //write to file
        CSVWriter writer = new CSVWriter(new FileWriter(outputFile));
        writer.writeAll(posts);
        writer.close();
    }
}
