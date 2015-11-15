package org.aldeon.peersim.protocol.helpers;

import org.aldeon.peersim.protocol.models.Id;
import org.aldeon.peersim.protocol.models.Post;

public class CsvTreeReader {

    private String sourcePath;

    public CsvTreeReader(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public ArrayList<Post> GetPostsTree() {
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

                if(values.length != 2)
                    break;

                String postFullId = values[0].replaceAll(csvQuoteChar, "");
                String parentFullId = values[1].replaceAll(csvQuoteChar, "");

                long postId = Long.parseLong(postFullIdSplit[1]);
                long parentId = Long.parseLong(parentFullIdSplit[1]);

                Post newPost = new Post(new Id(postId), new Id(parentId));

                posts.add(newPost);
            }

            System.out.println("Number of messages loaded from CSV file: " + posts.size());

            return posts;

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
    }
}
