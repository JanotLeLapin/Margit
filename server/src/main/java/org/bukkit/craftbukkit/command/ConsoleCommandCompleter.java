package org.bukkit.craftbukkit.command;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;
import org.jline.reader.Candidate;
// Margit start
// import jline.console.completer.Completer;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import net.minecraft.server.MinecraftServer;
// Margit end

public class ConsoleCommandCompleter implements Completer {
    // Margit start
    // private final CraftServer server;
    private final MinecraftServer server;
    // Margit end

    public ConsoleCommandCompleter(MinecraftServer server) {
        this.server = server;
    }

    public void complete(LineReader reader, ParsedLine parsedLine, final List<Candidate> candidates) { // Margit
        Waitable<List<String>> waitable = new Waitable<List<String>>() {
            @Override
            protected List<String> evaluate() {
                return server.server.getCommandMap().tabComplete(server.server.getConsoleSender(), parsedLine.line()); // Margit
            }
        };
        this.server.server.getServer().processQueue.add(waitable); // Margit
        try {
            List<String> offers = waitable.get();
            if (offers == null) {
                return; // Margit
            }
            // Margit start
            // candidates.addAll(offers);
            for (String offer : offers) {
                candidates.add(new Candidate(offer));
            }

            /*
            final int lastSpace = parsedLine.line().lastIndexOf(' ');
            if (lastSpace == -1) {
                return cursor - buffer.length();
            } else {
                return cursor - (buffer.length() - lastSpace - 1);
            }
            */
            // Margit end
        } catch (ExecutionException e) {
            this.server.server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e); // Margit
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return; // Margit
    }
}
