import java.io.File;
import java.util.*;

public class Executor {
    public static void runExternal(String input) {
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
            System.out.println(cmd + ": command not found");
            return;
        }

        try {
            // 👇 key change: set fullPath for launching, but keep cmd as Arg[0]
            List<String> commandWithArgs = new ArrayList<>();
            commandWithArgs.add(fullPath); // used to launch
            for (int i = 1; i < parts.length; i++) {
                commandWithArgs.add(parts[i]);
            }

            // 👇 Create ProcessBuilder with full path, but override Arg[0] to just cmd
            ProcessBuilder pb = new ProcessBuilder(commandWithArgs);
            pb.command().set(0, cmd); // This tricks argv[0] into being just "custom_exe_xxx"
            pb.inheritIO();
            pb.start().waitFor();

        } catch (Exception e) {
            System.out.println("Error running command: " + e.getMessage());
        }
    }
}
