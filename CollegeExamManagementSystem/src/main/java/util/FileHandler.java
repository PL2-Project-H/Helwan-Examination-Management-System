package util;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileHandler {

    private static final Path DATA_DIR = Paths.get("data");

    public static List<String> readAllLines(String filename) throws IOException {
        return Files.readAllLines(DATA_DIR.resolve(filename));
    }

    public static void writeAllLines(String filename, List<String> lines) throws IOException {
        Files.write(DATA_DIR.resolve(filename), lines);
    }

    public static void appendLine(String filename, String line) throws IOException {
        Files.write(
                DATA_DIR.resolve(filename),
                (line + System.lineSeparator()).getBytes(),
                StandardOpenOption.APPEND
        );
    }
}

