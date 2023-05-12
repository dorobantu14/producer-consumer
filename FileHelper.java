import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileHelper {

    static boolean firstProductOut = true;
    static boolean firstOrderOut = true;
    public static void markAsShipped(String fileName, String idCmd, String idProd) {
        String content = idCmd + "," + idProd + ",shipped\n";
        if (firstProductOut) {
            firstProductOut = false;
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(fileName);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.write(
                    Paths.get(fileName),
                    content.getBytes(),
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void markAsShipped(String fileName, String idCmd, int noProd) {
        String content = idCmd + "," + noProd + ",shipped\n";
        if (firstOrderOut) {
            firstOrderOut = false;
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(fileName);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.write(
                    Paths.get(fileName),
                    content.getBytes(),
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readAllLinesOfFile(String fileName) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
