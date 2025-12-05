package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

    private static final String DATA_DIR = "data/";

    public static List<String> readAllLines(String filename) throws IOException {
        return Files.readAllLines(Paths.get(DATA_DIR + filename));
    }

    public static void writeAllLines(String filename, List<String> lines) throws IOException {
        Files.write(Paths.get(DATA_DIR + filename), lines);
    }

    public static void appendLine(String filename, String line) throws IOException {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(DATA_DIR + filename, true))) {
            writer.write(line);
            writer.newLine();
        }
    }
}
