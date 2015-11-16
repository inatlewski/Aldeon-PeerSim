package org.aldeon.peersim.protocol.helpers;

import com.opencsv.CSVReader;
import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvTreeReader {

    private String sourceFile;

    public CsvTreeReader(String sourcePath) {
        this.sourceFile = sourcePath;
    }

    public ArrayList<Post> GetPostsTree() throws IOException {

        System.out.println("CSV source file path: " + sourceFile);

        ArrayList<Post> posts = new ArrayList<>();
        CSVReader csvReader = null;
        String[] row;

        try {
            csvReader = new CSVReader(new FileReader(sourceFile));
            List content = csvReader.readAll();

            for (Object object : content) {
                row = (String[]) object;

                long postId = Long.parseLong(row[0]);
                long parentId = Long.parseLong(row[1]);

                Post newPost = new Post(new Id(postId), new Id(parentId));

                posts.add(newPost);
            }

            System.out.println("Number of messages loaded from CSV file: " + posts.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            csvReader.close();

            return posts;
        }
    }
}
