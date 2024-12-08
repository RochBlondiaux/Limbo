package me.rochblondiaux.limbo.server.console;

import java.io.IOException;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.command.CommandSender;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

@Log4j2
public class LimboConsole extends SimpleTerminalConsole implements CommandSender {

    private static final PlainTextComponentSerializer PLAIN_TEXT = PlainTextComponentSerializer.plainText();

    private final Limbo app;

    public LimboConsole(Limbo app) throws IOException {
        this.app = app;
    }

    @Override
    protected boolean isRunning() {
        return this.app.running().get();
    }

    @Override
    protected void runCommand(String command) {
        this.app.commands().handleExecute(command);
    }

    @Override
    protected void shutdown() {
        this.app.stop();
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(
                builder.appName("Limbo")
                        .completer(new LimboCommandCompleter(this.app))
        );
    }

    @Override
    public String getName() {
        return "Console";
    }

    // Permissions

    @Override
    public void addPermission(String permission) {
        // Unused
    }

    @Override
    public void removePermission(String permission) {
        // Unused
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void clearPermissions() {
        // Unused
    }

    @Override
    public @Unmodifiable Set<String> permissions() {
        return Set.of();
    }

    // Audience
    @Override
    public void sendMessage(final @NotNull Identity source, final @NotNull Component message, final @NotNull MessageType type) {
        String plain = PLAIN_TEXT.serialize(message);
        log.info(plain);
    }
}
