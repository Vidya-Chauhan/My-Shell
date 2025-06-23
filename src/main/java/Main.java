import java.io.File;
import java.util.Scanner;

public class Main {
    public static File currentDirectory = new File (System.getProperty("user.dir"));
    public static void main(String[] args) throws Exception {
        //Uncomment this block to pass the first stage
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
                   
            
           if(Builtins.handleBuiltin(input)){
               Executor.runExternal(input);

            
           }
           System.out.flush();
        }
    }
}