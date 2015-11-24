package org.aldeon.helpers;

import com.opencsv.CSVReader;
import org.aldeon.model.Forest;
import org.aldeon.model.HashForest;

import java.io.FileReader;
import java.io.IOException;

public class CsvTreeReader {

    private String sourceFile;

    public CsvTreeReader(String sourcePath) {
        this.sourceFile = sourcePath;
    }

    public Forest GetPostsTree() throws IOException {

        try (CSVReader csvReader = new CSVReader(new FileReader(sourceFile))) {
            Forest forest = new HashForest();
            csvReader.readAll().forEach(row -> {
                long child = Long.parseLong(row[0]);
                long parent = Long.parseLong(row[1]);
                forest.add(parent, child);
            });
            return forest;
        }
    }
}
