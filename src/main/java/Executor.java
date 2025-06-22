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

        // Search PATH for full path
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
            // Prepare args list with `cmd` as Arg#0 (not fullPath)
            List<String> args = new ArrayList<>();
            args.add(fullPath); // This is used for launching
            for (int i = 1; i < parts.length; i++) {
                args.add(parts[i]);
            }

            // Launch process with full path but preserve Arg#0 as just the name
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.inheritIO();
            Map<String, String> env = pb.environment();
            env.put("PATH", System.getenv("PATH")); // Keep env safe

            Process process = pb.start();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("Error running command: " + e.getMessage());
        }
    }
}
