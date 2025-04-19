package dev.rocco.kig.paper.impl.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KigPaperCommand extends Command {
    public KigPaperCommand() {
        super("kigpaper");
        this.setPermission("kig.paper.command");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return true;
    }
}
