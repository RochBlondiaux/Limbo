package me.rochblondiaux.limbo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.command.CommandManager;
import me.rochblondiaux.limbo.command.implementation.HelpCommand;
import me.rochblondiaux.limbo.command.implementation.StopCommand;
import me.rochblondiaux.limbo.command.model.Command;
import me.rochblondiaux.limbo.configuration.ServerConfiguration;
import me.rochblondiaux.limbo.server.LimboServer;
import me.rochblondiaux.limbo.server.console.LimboConsole;

@Log4j2(topic = "Limbo")
@Getter
public class Limbo {

    private final Path dataFolder;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final LimboConsole console;
    private final LimboServer server;
    private final ServerConfiguration configuration;
    private final CommandManager commands;

    public Limbo() {
        log.info("Starting limbo...");
        long start = System.currentTimeMillis();

        this.dataFolder = Paths.get("").toAbsolutePath();

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::terminate, "Limbo Shutdown Thread"));

        // Commands
        this.commands = new CommandManager(this);
        this.commands.register(Command.builder()
                .name("stop")
                .aliases(Set.of("shutdown"))
                .usage("/stop")
                .description("Stops the server")
                .executor(new StopCommand(this))
                .build());
        this.commands.register(Command.builder()
                .name("help")
                .usage("/help [command]")
                .description("Displays help for a command")
                .executor(new HelpCommand(this))
                .build());

        // Console
        try {
            this.console = new LimboConsole(this);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize console", e);
        }

        // Configuration
        log.info("Loading configuration...");
        this.configuration = new ServerConfiguration(this.dataFolder);
        try {
            this.configuration.load();
        } catch (Exception e) {
            log.error("Failed to load configuration", e);
            System.exit(1);
        }
        log.info("Configuration loaded successfully");

        // Server
        this.server = new LimboServer(this);
        this.server.start();

        log.info("Limbo started in {}ms", System.currentTimeMillis() - start);

        // Start console
        this.console.start();
    }

    public void stop() {
        this.running.set(false);
    }

    private void terminate() {
        log.info("Stopping limbo...");
        long start = System.currentTimeMillis();

        // Server
        this.server.stop();

        log.info("Limbo stopped in {}ms", System.currentTimeMillis() - start);
    }
}
