package org.aldeon.peersim.protocol.helpers;

import com.opencsv.CSVReader;
import org.aldeon.peersim.protocol.model.Tree;
import org.javatuples.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class CsvTreeReader {

    private String sourceFile;

    public CsvTreeReader(String sourcePath) {
        this.sourceFile = sourcePath;
    }

    public Tree GetPostsTree() throws IOException {

        try (CSVReader csvReader = new CSVReader(new FileReader(sourceFile))) {
            return Tree.build(csvReader.readAll().stream()
                    .map(row -> new Pair<>(Long.parseLong(row[1]), Long.parseLong(row[0])))
                    .collect(Collectors.toList())
            );
        }
    }
}
