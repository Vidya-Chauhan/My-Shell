import java.io.File;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        System.setProperty("org.jline.utils.Log.level", "OFF");

        // Silencing STDERR
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

        // ✅ ArgumentCompleter will REPLACE word on <TAB>
         StringsCompleter completer = new StringsCompleter("echo", "exit");


        LineReader reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .completer(completer)
            .parser(parser)
            .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
            .build();

        // ✅ THIS LINE is CRUCIAL: allows completion on <TAB>
        reader.unsetOpt(LineReader.Option.INSERT_TAB);

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
