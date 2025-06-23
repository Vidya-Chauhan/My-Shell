import java.io.File;
import java.util.Scanner;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void printPrompt() {
        System.out.print("$ ");
        System.out.flush();  // ✅ Force it out instantly
    }

    public static void main(String[] args) throws Exception {
        new File("/tmp/apple").mkdirs(); 
        Scanner scanner = new Scanner(System.in);

        printPrompt(); // ✅ Initial prompt before anything

        while (true) {
            String input = scanner.nextLine();

            int result = Builtins.handleBuiltin(input);

            if (result == -1) {
                Executor.runExternal(input);
                printPrompt(); // after external
            } else if (result == 0) {
                printPrompt(); // after builtin
            }
            // if result == 1, prompt was already printed manually (e.g. after error)
        }
    }
}
