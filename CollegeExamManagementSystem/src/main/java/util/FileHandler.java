package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    private static final String DATA_DIR = "data/";

    static {
        // Ensure data directory exists
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println("Created data directory: " + DATA_DIR);
            }
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    public static List<String> readAllLines(String filename) {
        List<String> lines = new ArrayList<>();
        Path filePath = Paths.get(DATA_DIR + filename);
        
        try {
            // Create file if it doesn't exist
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Created file: " + filename);
            }
            
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file " + filename + ": " + e.getMessage());
        }
        
        return lines;
    }

    public static void writeAllLines(String filename, List<String> lines) {
        Path filePath = Paths.get(DATA_DIR + filename);
        
        try {
            // Create file if it doesn't exist
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void appendLine(String filename, String line) {
        Path filePath = Paths.get(DATA_DIR + filename);
        
        try {
            // Create file if it doesn't exist
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error appending to file " + filename + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if a file exists in the data directory
     */
    public static boolean fileExists(String filename) {
        return Files.exists(Paths.get(DATA_DIR + filename));
    }

    /**
     * Initialize essential data files if they don't exist
     */
    public static void initializeDataFiles() {
        String[] essentialFiles = {
            "users.txt",
            "subjects.txt",
            "exams.txt",
            "questions.txt",
            "grades.txt",
            "student_subjects.txt",
            "lecturer_subjects.txt",
            "feedback.txt",
            "student_attempts.txt",
            "student_answers.txt",
            "re_correction_requests.txt"
        };

        for (String filename : essentialFiles) {
            Path filePath = Paths.get(DATA_DIR + filename);
            try {
                if (!Files.exists(filePath)) {
                    Files.createFile(filePath);
                    System.out.println("Initialized file: " + filename);
                }
            } catch (IOException e) {
                System.err.println("Error initializing file " + filename + ": " + e.getMessage());
            }
        }
    }
}
