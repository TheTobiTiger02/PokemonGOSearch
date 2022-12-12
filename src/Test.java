import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Test {
    public static void main(String[] args) {
        // The path to the file to edit
        String filePath = "pokemonList.txt";

        // The line to insert
        String lineToInsert = "This is the line to insert.";

        // The line number at which to insert the new line
        // Note: The first line of the file is line 1, not line 0
        int lineNumber = 5;

        // Open the file and create a BufferedReader to read from it
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            // Create a temporary file to hold the modified file contents
            Path tempFilePath = Files.createTempFile("file", ".tmp");
            try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath, StandardCharsets.UTF_8)) {
                // Read each line from the original file
                String line;
                int currentLine = 1;
                while ((line = reader.readLine()) != null) {
                    // If the current line is the line where the new line should be inserted,
                    // write the new line to the temp file
                    if (currentLine == lineNumber) {
                        writer.write(lineToInsert);
                        writer.newLine();
                    }

                    // Write the current line to the temp file
                    writer.write(line);
                    writer.newLine();

                    // Increment the line counter
                    currentLine++;
                }
            }

            // Replace the original file with the temp file
            Files.move(tempFilePath, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("An error occurred while reading or writing the file: " + e.getMessage());
        }
    }
}
