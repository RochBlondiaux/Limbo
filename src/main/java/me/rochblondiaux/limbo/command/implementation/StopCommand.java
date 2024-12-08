package me.rochblondiaux.limbo.command.implementation;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.command.CommandSender;
import me.rochblondiaux.limbo.command.model.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

@RequiredArgsConstructor
public class StopCommand implements CommandExecutor {

    private final Limbo app;

    @Override
    public void execute(CommandSender sender, String command, String[] args) {
        // Permission
        if (!sender.hasPermission("limbo.stop")) {
            sender.sendMessage(Component.text(
                    "You do not have permission to execute this command.",
                    NamedTextColor.RED,
                    TextDecoration.BOLD
            ));
            return;
        }

        // Stop the server
        sender.sendMessage(Component.text("Stopping the server...", NamedTextColor.RED, TextDecoration.BOLD));
        this.app.stop();
    }
}
