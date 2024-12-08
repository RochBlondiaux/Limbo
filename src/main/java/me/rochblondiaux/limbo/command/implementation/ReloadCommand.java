package me.rochblondiaux.limbo.command.implementation;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.command.CommandSender;
import me.rochblondiaux.limbo.command.model.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

@RequiredArgsConstructor
public class ReloadCommand implements CommandExecutor {

    private final Limbo app;

    @Override
    public void execute(CommandSender sender, String command, String[] args) {
        // Permission
        if (!sender.hasPermission("limbo.reload")) {
            sender.sendMessage(Component.text(
                    "You do not have permission to execute this command.",
                    NamedTextColor.RED,
                    TextDecoration.BOLD
            ));
            return;
        }

        // Reload
        sender.sendMessage(Component.text(
                "Reloading...",
                NamedTextColor.GREEN,
                TextDecoration.BOLD
        ));

        try {
            sender.sendMessage(Component.text(
                    "Reloaded.",
                    NamedTextColor.GREEN,
                    TextDecoration.BOLD
            ));

            app.configuration().load();
        } catch (Exception e) {
            sender.sendMessage(Component.text(
                    "An error occurred while reloading.",
                    NamedTextColor.RED,
                    TextDecoration.BOLD
            ));
            e.printStackTrace();
        }
    }

}
