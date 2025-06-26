import java.io.File;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        // Turn off JLine logging
        System.setProperty("org.jline.utils.Log.level", "OFF");

        // Optional: suppress stderr completely
        System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
            public void write(int b) {}
        }));

        // Terminal config that avoids infocmp
        Terminal terminal = TerminalBuilder.builder()
            .dumb(true)
            .streams(System.in, System.out)
            .build();

        // Line reader with autocomplete for built-ins
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
