package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
// Margit start
// import jline.console.ConsoleReader;
import org.jline.reader.LineReader;
// Margit end
import com.mojang.util.QueueLogAppender;
import org.bukkit.craftbukkit.Main;

public class TerminalConsoleWriterThread implements Runnable {
    // Margit start
    /*
    final private ConsoleReader reader;
    */
    final private LineReader reader;
    // Margit end
    final private OutputStream output;

    public TerminalConsoleWriterThread(OutputStream output, /* Margit ConsoleReader */ LineReader reader) {
        this.output = output;
        this.reader = reader;
    }

    public void run() {
        String message;

        // Using name from log4j config in vanilla jar
        while (true) {
            message = QueueLogAppender.getNextLogEvent("TerminalConsole");
            if (message == null) {
                continue;
            }

            try {
                if (Main.useJline) {
                    // Margit start
                    /*
                    reader.print(ConsoleReader.RESET_LINE + "");
                    reader.flush();
                    */

                    reader.callWidget(LineReader.CLEAR);
                    reader.getTerminal().writer().flush();

                    output.write(message.getBytes());
                    output.flush();

                    try {
                        // reader.drawLine();
                        reader.callWidget(LineReader.REDRAW_LINE);
                        reader.callWidget(LineReader.REDISPLAY);
                    } catch (Throwable ex) {
                        // reader.getCursorBuffer().clear();
                        reader.getBuffer().clear();
                    }
                    // reader.flush();
                    reader.getTerminal().writer().flush();
                    // Margit end
                } else {
                    output.write(message.getBytes());
                    output.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(TerminalConsoleWriterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
