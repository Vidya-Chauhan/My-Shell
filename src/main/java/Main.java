import java.io.File;
import java.util.Scanner;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void printPrompt() {
        System.out.print("$ ");
        System.out.flush();
      
    }

    public static void main(String[] args) throws Exception {
        new File("/tmp/apple/local/bin").mkdirs(); 

        try (Scanner scanner = new Scanner(System.in)) {
            printPrompt(); 

            while (true) {
                String input = scanner.nextLine();

                int result = Builtins.handleBuiltin(input);

                if (result == -1) {
                    Executor.runExternal(input);
                   
                } 
                printPrompt();
                
                
            }
        }
    }
}
