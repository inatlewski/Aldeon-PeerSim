package org.aldeon.confgen;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    private static final Map<String, String> MUTATORS = ImmutableMap.of(
            "uniform", "org.aldeon.mutator.UniformMutator",
            "leaf", "org.aldeon.mutator.UniformLeafMutator"
    );

    private static final Set<String> TREES = ImmutableSet.of(
            "balanced",
            "furry",
            "single",
            "wide"
    );

    private static final Map<Integer, String> SIZES = ImmutableMap.of(
            10000, "10k",
            100000, "100k"
            //1000000, "1M"
    );

    public static void main(String[] args) throws IOException {
        String template = template();

        for (String tree: TREES) {
            for (Map.Entry<String, String> mutator: MUTATORS.entrySet()) {
                for (Map.Entry<Integer, String> size: SIZES.entrySet()) {
                    for (boolean suggests: Lists.newArrayList(true, false)) {
                        int currentSize = 1;

                        while (currentSize < size.getKey()) {
                            String fileName = "instances/" + tree + "/" + size.getValue() + ".csv";

                            String newTemplate = template
                                    .replace("%INPUT_FILE%", fileName)
                                    .replace("%MUTATOR%", mutator.getValue())
                                    .replace("%DIFF_COUNT%", currentSize + "")
                                    .replace("%SUGGESTS%", suggests ? "true" : "false");

                            String cfgName = mutator.getKey()
                                    + "_" + tree + "_" + size.getValue() + "_"
                                    + currentSize + (suggests ? "_suggest" : "")
                                    + ".cfg";
                            Files.write(Paths.get("config/" + cfgName), Lists.newArrayList(newTemplate), Charsets.UTF_8);

                            currentSize *= 2;
                        }
                    }
                }
            }
        }

    }

    public static String template() throws IOException {
        return Files.lines(Paths.get("config/config.template"), Charsets.UTF_8).collect(Collectors.joining("\n"));
    }
}
