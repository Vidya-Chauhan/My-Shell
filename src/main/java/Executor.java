import java.io.File;
import java.util.*;

public class Executor {
    public static void runExternal(String input){
        String[] parts = input.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            System.out.println(": command not found");
            return;
        }

        String cmd = parts[0];
        String fullPath = null;

        for (String dir : System.getenv("PATH").split(":")) {
            File file = new File(dir, cmd);
            if (file.exists() && file.canExecute()) {
                fullPath = file.getAbsolutePath();
                break;
            }
        }

        if (fullPath == null) {
            System.out.println(cmd + ": command not found");  // ✅ Fix 1
        } else {
            try {
                List<String> commandWithArgs = new ArrayList<>();
                commandWithArgs.add(fullPath);  // ✅ Fix 2
                for (int i = 1; i < parts.length; i++) {
                    commandWithArgs.add(parts[i]);
                }

                ProcessBuilder pb = new ProcessBuilder(commandWithArgs);
                pb.inheritIO();
                Process process = pb.start();
                process.waitFor();
            } catch (Exception e) {
                System.out.println("Error running command: " + e.getMessage());
            }
        }
    }
}
