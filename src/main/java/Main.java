import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        //Uncomment this block to pass the first stage
        
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
                   
            
            if (input.equals("exit 0")) {
                System.exit(0); 
            }
            else if(input.startsWith("echo ")){
            System.out.println(input.substring(5));
        }
            else if(input.startsWith("type ")){
            String[] parts = input.split("\\s+");
            if(parts.length == 2){
                String cmd = parts[1];
                if(cmd.equals("echo")|| cmd.equals("exit")|| cmd.equals("type")){
                    System.out.println(cmd + " is a shell builtin");
                } else {
                    System.out.println(cmd + ": not found");
                }
            } else {
                System.out.println("Usage: type <command>");
            }
        }
        else {
            System.out.println(input + ": command not found");
        }
        }
    }
   
}

