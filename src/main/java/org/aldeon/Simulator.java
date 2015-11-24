package org.aldeon;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class Simulator {

    public static int result = -1;

    public static void main(String[] args) throws IOException {
        String cfgFile = args[0];
        peersim.Simulator.main(args);
        Files.append(cfgFile + " : " + result + "\n", new File("result.txt"), Charsets.UTF_8);
    }
}
