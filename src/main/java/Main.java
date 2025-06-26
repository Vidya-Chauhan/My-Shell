import java.io.File;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        System.setProperty("org.jline.utils.Log.level", "OFF");

        System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
            public void write(int b) {}
        }));

        Terminal terminal = TerminalBuilder.builder()
            .dumb(true)
            .streams(System.in, System.out)
            .build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(new char[0]);
        parser.setEofOnEscapedNewLine(true);

        // ✅ ArgumentCompleter ensures full-word replacement
        Completer completer = new ArgumentCompleter(
            new StringsCompleter("echo", "exit"),
            NullCompleter.INSTANCE
        );

        LineReader reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .completer(completer)
            .parser(parser)
            .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
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
