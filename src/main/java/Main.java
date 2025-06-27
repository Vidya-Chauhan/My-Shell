import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws IOException {
        List<String> builtins = Arrays.asList("echo", "exit", "type", "pwd", "cd");

        TabCompletion tabCompletion = new TabCompletion(builtins);

        while (true) {
            System.out.print("$ ");
            String input;
            try {
                input = tabCompletion.readLine();
            } catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
                break;
            }

            if (input.trim().isEmpty()) continue;

            int result;
            try {
                result = Builtins.handleBuiltin(input);
            } catch (Exception e) {
                System.err.println("Error executing builtin: " + e.getMessage());
                continue;
            }

            if (result == -1) {
                Executor.runExternal(input);
            }
        }
    }
}
