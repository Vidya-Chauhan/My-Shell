import java.io.File;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        // ✅ Turn off JLine logging
        System.setProperty("org.jline.utils.Log.level", "OFF");

        Terminal terminal = TerminalBuilder.builder()
            .dumb(true)                         // Prevents infocmp issue
            .streams(System.in, System.out)     // Uses standard input/output
            .build();

        LineReader reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .completer(new StringsCompleter("echo", "exit"))
            .build();

        while (true) {
            String input;
            try {
                input = reader.readLine("$ ");
            } catch (UserInterruptException | EndOfFileException e) {
                break;
            }

            int result = Builtins.handleBuiltin(input);
            if (result == -1) {
                Executor.runExternal(input);
            }
        }
    }
}
