package me.rochblondiaux.limbo.command.model;

import me.rochblondiaux.limbo.command.CommandSender;

public interface CommandExecutor {

    void execute(CommandSender sender, String command, String[] args);

}
