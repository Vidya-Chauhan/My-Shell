import java.io.File;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        Terminal terminal = TerminalBuilder.builder()
    .jna(false)  // 🔴 disable native loading
    .jansi(false) // 🔴 disable ANSI native
    .dumb(true)   // ✅ fallback to basic terminal
    .system(true)
    .build();


        // Autocomplete for built-in commands
        LineReader reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .completer(new StringsCompleter("echo", "exit"))
            .build();

        while (true) {
            String input;
            try {
                input = reader.readLine("$ ");
            } catch (UserInterruptException | EndOfFileException e) {
                break; // Exit on Ctrl+C or Ctrl+D
            }

            int result = Builtins.handleBuiltin(input);

            if (result == -1) {
                Executor.runExternal(input);
            }
        }
    }
}
