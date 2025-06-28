import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws IOException {
        List<String> builtins = Arrays.asList("echo", "exit");

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

            if (input == null || input.trim().isEmpty()) continue;

            if (input.startsWith("echo ")) {
                System.out.println(input.substring(5));
            } else if (input.equals("exit") || input.equals("exit 0")) {
                System.exit(0);
            }
        }
    }
}
