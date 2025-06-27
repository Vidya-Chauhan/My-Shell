import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Builtins {

    public static int handleBuiltin(String input) throws IOException {
        if (input.trim().isEmpty()) return 0;

        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0];
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (command) {
            case "exit":
                System.exit(0);
                return 0;

            case "echo":
                System.out.println(String.join(" ", args));
                return 0;

            case "pwd":
                System.out.println(Main.currentDirectory.getCanonicalPath());
                return 0;

            case "cd":
                if (args.length == 0) {
                    System.out.println("Usage: cd <path>");
                    return 0;
                }
                String path = args[0];
                if (path.equals("~")) {
                    path = System.getenv("HOME");
                }
                try {
                    File target = path.startsWith("/") ?
                        new File(path).getCanonicalFile() :
                        new File(Main.currentDirectory, path).getCanonicalFile();

                    if (target.exists() && target.isDirectory()) {
                        Main.currentDirectory = target;
                    } else {
                        System.out.println("cd: " + path + ": No such file or directory");
                    }

                } catch (Exception e) {
                    System.out.println("cd: " + path + ": No such file or directory");
                }
                return 0;

            case "type":
                if (args.length == 0) {
                    System.out.println("Usage: type <command>");
                    return 0;
                }
                for (String cmd : args) {
                    if (isBuiltin(cmd)) {
                        System.out.println(cmd + " is a shell builtin");
                    } else {
                        String[] pathDirs = System.getenv("PATH").split(":");
                        boolean found = false;
                        for (String dir : pathDirs) {
                            File file = new File(dir, cmd);
                            if (file.exists() && file.canExecute()) {
                                System.out.println(cmd + " is " + file.getAbsolutePath());
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            System.out.println(cmd + ": not found");
                        }
                    }
                }
                return 0;

            default:
                return -1;
        }
    }

    private static boolean isBuiltin(String cmd) {
        return cmd.equals("echo") || cmd.equals("exit") || cmd.equals("type") || cmd.equals("pwd") || cmd.equals("cd");
    }
}
