package me.rochblondiaux.limbo.command.model;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Command {

    private final String name;
    private final Set<String> aliases;
    private final String description;
    private final String usage;
    private final CommandExecutor executor;
    private final @Nullable TabCompleter tabCompleter;

}
