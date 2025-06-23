import java.io.File;

public class Builtins {
    public static boolean handleBuiltin(String input) {
        if (input.equals("exit 0")) {
            System.exit(0);
        }

        if (input.startsWith("echo")) {
            System.out.println(input.substring(5));
            return true;
        }

        if (input.startsWith("pwd")) {
            System.out.println(Main.currentDirectory.getAbsolutePath());
            return true;
        }

        if (input.startsWith("cd")) {
            String[] parts = input.split("\\s+");

            if (parts.length == 2) {
                String path = parts[1];
                try {
                    File target = new File(Main.currentDirectory, path).getCanonicalFile();
                    if (target.exists() && target.isDirectory()) {
                        Main.currentDirectory = target;
                    } else {
                        System.out.print("cd: " + path + ": No such file or directory\n");
System.out.flush();
Main.printPrompt();
                    }
                } catch (Exception e) {
                   System.out.print("cd: " + path + ": Error resolving path\n");
            System.out.flush();
                    Main.printPrompt();
                }
            } else {
                System.out.println("Usage: cd <path>\n");
                System.out.flush();
                Main.printPrompt();
            }
            return true;
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
                            return true;
                        }
                    }
                    System.out.println(cmd + ": not found");
                }
            } else {
                System.out.println("Usage: type <command>");
            }
            return true;
        }

        return false;
    }
}
