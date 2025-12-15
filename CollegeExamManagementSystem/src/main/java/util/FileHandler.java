package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    private static final String DATA_DIR = "data/";

    public static List<String> readAllLines(String filename) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(DATA_DIR + filename));
        } catch (IOException e) {
            System.err.println("Error reading file " + filename + ": " + e.getMessage());
        }
        return lines;
    }

    public static void writeAllLines(String filename, List<String> lines) {
        try {
            Files.write(Paths.get(DATA_DIR + filename), lines);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e.getMessage());
        }
    }

    public static void appendLine(String filename, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + filename, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to file " + filename + ": " + e.getMessage());
        }
    }
}
