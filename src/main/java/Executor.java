import java.io.File;
import java.util.*;

public class Executor {

    public static List<String> parseCommand(String input) {
    List<String> parts = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean inSingleQuote = false;

    for (int i = 0; i < input.length(); i++) {
        char c = input.charAt(i);

        if (c == '\'') {
            inSingleQuote = !inSingleQuote;  // Toggle quote mode
        } else if (Character.isWhitespace(c) && !inSingleQuote) {
            if (current.length() > 0) {
                parts.add(current.toString());
                current.setLength(0);
            }
        } else {
            current.append(c);
        }
    }

    if (current.length() > 0) {
        parts.add(current.toString());
    }

    return parts;
}

    public static void runExternal(String input) {
    List<String> parts = parseCommand(input);
    if (parts.size() == 0 || parts.get(0).isEmpty()) {
        System.out.println(": command not found");
        return;
    }

    String cmd = parts.get(0);
    String fullPath = null;

    for (String dir : System.getenv("PATH").split(":")) {
        File file = new File(dir, cmd);
        if (file.exists() && file.canExecute()) {
            fullPath = file.getAbsolutePath();
            break;
        }
    }

    if (fullPath == null) {
        System.out.println(cmd + ": command not found");
        return;
    }

    try {
        List<String> commandWithArgs = new ArrayList<>();
        commandWithArgs.add(fullPath);
        for (int i = 1; i < parts.size(); i++) {
            commandWithArgs.add(parts.get(i));
        }

        ProcessBuilder pb = new ProcessBuilder(commandWithArgs);
        pb.command().set(0, cmd);
        pb.directory(Main.currentDirectory);
        pb.inheritIO();

        pb.start().waitFor();

    } catch (Exception e) {
        System.out.println("Error running command: " + e.getMessage());
    }
}

}
