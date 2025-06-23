import java.io.File;
import java.util.Scanner;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void printPrompt() {
        System.out.print("$ ");
        System.out.flush();  // ✅ Very important
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printPrompt();
            String input = scanner.nextLine();

            if (!Builtins.handleBuiltin(input)) {
                Executor.runExternal(input);
            }
        }
    }
}
