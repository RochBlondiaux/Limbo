package me.rochblondiaux.limbo.command;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.Unmodifiable;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.command.model.Command;
import me.rochblondiaux.limbo.exception.command.CommandRegistrationException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@RequiredArgsConstructor
public class CommandManager {

    private final Limbo app;
    private final Set<Command> commands = new HashSet<>();

    public void register(Command command) throws CommandRegistrationException {
        if (this.findByName(command.name()).isPresent())
            throw new CommandRegistrationException("Command with name \"%s\" already registered.".formatted(command.name()));

        this.commands.add(command);
    }

    public void unregister(Command command) {
        this.commands.remove(command);
    }

    public void handleExecute(String raw) {
        String[] args = raw.split(" ");
        String name = args[0].contains("/") ? args[0].substring(1) : args[0];
        String[] processedArgs = args.length > 1 ? raw.substring(name.length() + 1).split(" ") : new String[0];

        this.findByName(name)
                .ifPresentOrElse(command -> command.executor().execute(this.app.console(), name, processedArgs),
                        () -> this.app.console().sendMessage(Component.text("Unknown command. Type \"help\" for help.", NamedTextColor.RED)));
    }

    public Optional<Command> findByName(String name) {
        return this.commands.stream()
                .filter(command -> command.name().equals(name) || command.aliases().contains(name))
                .findFirst();
    }

    @Unmodifiable
    public Set<Command> commands() {
        return Collections.unmodifiableSet(this.commands);
    }
}
