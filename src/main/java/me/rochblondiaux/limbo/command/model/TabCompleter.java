package me.rochblondiaux.limbo.command.model;

import java.util.List;

import me.rochblondiaux.limbo.command.CommandSender;

public interface TabCompleter {

    List<String> complete(CommandSender sender, String command, String[] args);

}
