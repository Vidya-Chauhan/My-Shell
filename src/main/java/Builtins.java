import java.io.File;

public class Builtins {
    public static int handleBuiltin(String input) {
        if (input.equals("exit 0")) {
            System.exit(0);
        }

        if (input.startsWith("echo")) {
            System.out.println(input.substring(5));
            return 0;  // prompt will be printed by Main
        }

        if (input.startsWith("pwd")) {
            System.out.println(Main.currentDirectory.getAbsolutePath());
            return 0;  // prompt will be printed by Main
        }

        if (input.startsWith("cd")) {
            String[] parts = input.split("\\s+");

            if (parts.length == 2) {
                String path = parts[1];
                try {
                    File target = new File(Main.currentDirectory, path).getCanonicalFile();
                    if (target.exists() && target.isDirectory()) {
                        Main.currentDirectory = target;
                        return 0; // success, prompt by Main
                    } else {
                        System.out.print("cd: " + path + ": No such file or directory\n");
                        System.out.flush();
                        Main.printPrompt(); // we already printed prompt
                        return 1;
                    }
                } catch (Exception e) {
                    System.out.print("cd: " + path + ": Error resolving path\n");
                    System.out.flush();
                    Main.printPrompt();
                    return 1;
                }
            } else {
                System.out.print("Usage: cd <path>\n");
                System.out.flush();
                Main.printPrompt();
                return 1;
            }
        }

        if (input.startsWith("type")) {
            String[] parts = input.split("\\s+");
            if (parts.length == 2) {
                String cmd = parts[1];
                if (cmd.equals("echo") || cmd.equals("exit") || cmd.equals("type") || cmd.equals("pwd") || cmd.equals("cd")) {
                    System.out.println(cmd + " is a shell builtin");
                } else {
                    String[] pathDirs = System.getenv("PATH").split(":");
                    for (String dir : pathDirs) {
                        File file = new File(dir, cmd);
                        if (file.exists() && file.canExecute()) {
                            System.out.println(cmd + " is " + file.getAbsolutePath());
                            return 0;
                        }
                    }
                    System.out.println(cmd + ": not found");
                }
            } else {
                System.out.println("Usage: type <command>");
            }
            return 0;
        }

        return -1; // not a builtin
    }
}
