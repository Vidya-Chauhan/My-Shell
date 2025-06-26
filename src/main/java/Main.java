import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static File currentDirectory = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws Exception {
        // Disable JLine logs
        Logger jlineLogger = Logger.getLogger("org.jline");
        jlineLogger.setLevel(Level.OFF);
        for (Handler handler : jlineLogger.getHandlers()) {
            handler.setLevel(Level.OFF);
        }

        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(new char[0]);  // ⬅️ Super important to prevent triple-space bug

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("echo", "exit"))  // ⬅️ Your builtins
                .parser(parser)
                .option(LineReader.Option.HISTORY_VERIFY, false)
                .option(LineReader.Option.HISTORY_BEEP, false)
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
