import java.io.File;
import java.util.*;

public class Executor {
public static void runExternal(String input) {

    String errorFile = null;
    boolean appendError = false;
    String inputWithoutStderr;
String[] errAppendSplit = input.split("\\s*2>>\\s*", 2);
if(errAppendSplit.length >1){
    appendError = true;
    errorFile = errAppendSplit[1].trim();
    inputWithoutStderr = errAppendSplit[0].trim();
} else {
     String[] errSplit = input.split("\\s*2>\\s*", 2);
    if (errSplit.length > 1) {
        errorFile = errSplit[1].trim();
        inputWithoutStderr = errSplit[0].trim();
    } else {
        inputWithoutStderr = input.trim();
    }
}


// Step 2: Handle stdout APPEND redirection (>> or 1>>)
String outputFile = null;
boolean appendOutput = false;

String[] appendSplit = inputWithoutStderr.split("(?<!\\d)\\s*>>\\s*|\\s*1>>\\s*", 2);
String commandPart;

if (appendSplit.length > 1) {
    commandPart = appendSplit[0].trim();
    outputFile = appendSplit[1].trim();
    appendOutput = true;
} else {
    // Step 3: Handle stdout OVERWRITE redirection (> or 1>)
    String[] outputSplit = inputWithoutStderr.split("(?<!\\d)\\s*>\\s*|\\s*1>\\s*", 2);
    if (outputSplit.length > 1) {
        commandPart = outputSplit[0].trim();
        outputFile = outputSplit[1].trim();
    } else {
        commandPart = inputWithoutStderr.trim();
    }
}



    List<String> parts = parseCommand(commandPart);
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
        pb.command().set(0, cmd); // restore user input command
        pb.directory(Main.currentDirectory);

        boolean isRedirecting = false;

        // Handle stdout redirection
if (outputFile != null && !outputFile.isEmpty()) {
    File outFile = new File(outputFile);
    File parent = outFile.getParentFile();
    if (parent != null && !parent.exists()) {
        parent.mkdirs();
    }

    if (appendOutput) {
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(outFile)); // ✅ append mode
    } else {
        pb.redirectOutput(outFile); // ✅ overwrite mode
    }

    isRedirecting = true;
} else {
    pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
}


// Handle stderr redirection
if (errorFile != null && !errorFile.isEmpty()) {
    File errFile = new File(errorFile);
    File parent = errFile.getParentFile();
    if (parent != null && !parent.exists()) {
        parent.mkdirs();
    }
    if(appendError){
    pb.redirectError(ProcessBuilder.Redirect.appendTo(errFile));
    }
   else {
    pb.redirectError(errFile);
} else {
    pb.redirectError(ProcessBuilder.Redirect.INHERIT);
}


        Process process = pb.start();
        process.waitFor();

        // ❌ Don't print anything here. Prompt is printed from Main only.
    } catch (Exception e) {
        System.out.println("Error running command: " + e.getMessage());
    }
}

public static List<String> parseCommand(String input) {
    List<String> parts = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    boolean escapeNextChar = false;

    for (int i = 0; i < input.length(); i++) {
        char c = input.charAt(i);

       
        if (escapeNextChar) {
            if (inDoubleQuote) {
                // Inside double quotes: only escape ", \ (and later $, `)
                if (c == '"' || c == '\\') {
                    current.append(c);
                } else {
                    current.append('\\').append(c); // preserve the backslash
                }
            } else if (!inSingleQuote) {
                current.append(c); // unquoted: escape next char
            } else {
                current.append('\\').append(c); // inside single quote, backslash is literal
            }
            escapeNextChar = false;
        } else if (c == '\\' && !inSingleQuote) {
            // escape active, but not inside single quotes
            escapeNextChar = true;
        } else if (c == '\'' && !inDoubleQuote) {
            inSingleQuote = !inSingleQuote;
        } else if (c == '"' && !inSingleQuote) {
            inDoubleQuote = !inDoubleQuote;
        } else if (Character.isWhitespace(c) && !inSingleQuote && !inDoubleQuote) {
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

}
