import java.io.File;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        // Disable jline logging and system error stream
        System.setProperty("org.jline.utils.Log.level", "OFF");

        System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
            public void write(int b) {}
        }));

        // Build terminal
        Terminal terminal = TerminalBuilder.builder()
            .dumb(true)
            .streams(System.in, System.out)
            .build();

        // ✅ Custom parser with no escape characters
        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(new char[0]);

        // ✅ Use StringsCompleter with "echo" and "exit"
        LineReader reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .parser(parser)
            .completer(new StringsCompleter("echo", "exit"))
            .build();

        // Shell loop
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
