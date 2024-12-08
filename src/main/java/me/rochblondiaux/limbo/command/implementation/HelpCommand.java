package me.rochblondiaux.limbo.command.implementation;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.command.CommandSender;
import me.rochblondiaux.limbo.command.model.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

@RequiredArgsConstructor
public class HelpCommand implements CommandExecutor {

    private final Limbo app;

    @Override
    public void execute(CommandSender sender, String command, String[] args) {
        // Permission
        if (!sender.hasPermission("limbo.help")) {
            sender.sendMessage(Component.text("You do not have permission to execute this command.", NamedTextColor.RED, TextDecoration.BOLD));
            return;
        }

        if (args.length == 0) {
            this.sendHelpMenu(sender);
            return;
        }

        String commandName = args[0];
        this.app.commands()
                .commands()
                .stream()
                .filter(command1 -> command1.name().equalsIgnoreCase(commandName))
                .findFirst()
                .ifPresentOrElse(command1 -> {
                    sender.sendMessage(Component.text("Help for command: ", NamedTextColor.GOLD)
                            .append(Component.text(command1.name(), NamedTextColor.GRAY)));
                    sender.sendMessage(Component.text("Description: ", NamedTextColor.GRAY)
                            .append(Component.text(command1.description(), NamedTextColor.GRAY)));
                    sender.sendMessage(Component.text("Usage: ", NamedTextColor.GRAY)
                            .append(Component.text(command1.usage(), NamedTextColor.GRAY)));
                }, () -> sender.sendMessage(Component.text("Command not found.", NamedTextColor.RED, TextDecoration.BOLD)));

    }

    private void sendHelpMenu(CommandSender sender) {
        sender.sendMessage(Component.text("Limbo Help Menu", NamedTextColor.GOLD, TextDecoration.BOLD));
        sender.sendMessage(Component.text("Below is a list of all available commands.", NamedTextColor.GRAY));
        this.app.commands()
                .commands()
                .forEach(command1 -> {
                    sender.sendMessage(Component.text(command1.name(), NamedTextColor.GRAY)
                            .append(Component.text(" - ", NamedTextColor.GRAY))
                            .append(Component.text(command1.description(), NamedTextColor.GRAY)));
                });
        sender.sendMessage(Component.empty());
    }

}
