import java.io.*;
import java.util.*;

public class TabCompletion {
    private String originalTerminalSettings;
    private List<String> strings;

    public TabCompletion(List<String> words) {
        strings = words;
        try {
            initializeTerminal();
        } catch (Exception e) {
            handleError("An error occurred: ", e);
        }
    }

    String readLine() throws IOException {
        StringBuilder input = new StringBuilder();

        while (true) {
            int key = System.in.read();

            if (key == 10) { // Enter
                System.out.println();
                return input.toString();
            } else if (key == 9) { // TAB
                String completion = findCompletion(input.toString());
                if (completion != null) {
                    clearCurrentLine(input.length());
                    input = new StringBuilder(completion);
                    input.append(" ");
                    System.out.print(completion + " ");
                } else {
                    System.out.print("\u0007");
                }
            } else if (key == 127 || key == 8) { // Backspace
                if (input.length() > 0) {
                    input.deleteCharAt(input.length() - 1);
                    System.out.print("\b \b");
                }
            } else if (key >= 32 && key <= 126) { // Printable characters
                input.append((char) key);
                System.out.print((char) key);
            }
        }
    }

    private String findCompletion(String prefix) {
        return strings.stream()
                .filter(word -> word.startsWith(prefix))
                .findFirst()
                .orElse(null);
    }

    private void clearCurrentLine(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("\b \b");
        }
    }

    private void initializeTerminal() throws IOException, InterruptedException {
        originalTerminalSettings = stty("--save");
        stty("-echo -icanon");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                stty(originalTerminalSettings);
            } catch (Exception e) {
                handleError("Failed to restore terminal: ", e);
            }
        }));
    }

    private String stty(String args) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("sh", "-c", "stty " + args + " < /dev/tty").start();
        return readOutput(process);
    }

    private String readOutput(Process process) throws IOException {
        try (InputStream in = process.getInputStream()) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString().trim();
        }
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + e.getMessage());
    }
}
