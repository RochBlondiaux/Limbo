package me.rochblondiaux.limbo.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import lombok.Getter;
import me.rochblondiaux.limbo.exception.configuration.ConfigurationLoadException;
import me.rochblondiaux.limbo.exception.configuration.ConfigurationSaveException;

@Getter
public class ServerConfiguration {

    public static final String COMMENT = "For explanation of what each of the options does, please visit:\nhttps://github.com/RochBlondiaux/Limbo/blob/master/src/main/resources/configuration/server.properties";

    private final Path path;
    private Properties properties;

    // Configuration options
    private String host;
    private int port;
    private int maxPlayers;

    public ServerConfiguration(Path dataFolder) {
        this.path = dataFolder.resolve("server.properties");
    }

    public void load() throws ConfigurationLoadException {
        // Default configuration
        Properties definition = new Properties();
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("configuration/server.properties")) {
            definition.load(in);
        } catch (IOException e) {
            throw new ConfigurationLoadException("Failed to load default configuration", e);
        }

        // Load the configuration file
        this.properties = new Properties();
        if (Files.exists(this.path))
            try (InputStream in = Files.newInputStream(path)) {
                properties.load(in);
            } catch (IOException e) {
                throw new ConfigurationLoadException("Failed to load configuration file", e);
            }

        // Add missing properties
        definition.forEach((key, value) -> properties.putIfAbsent(key, value));

        // Save the configuration file
        try {
            this.save();
        } catch (ConfigurationSaveException e) {
            throw new ConfigurationLoadException("Failed to save configuration file", e);
        }

        // Load the configuration options
        this.host = properties.getProperty("host");
        this.port = Integer.parseInt(properties.getProperty("port"));
        this.maxPlayers = Integer.parseInt(properties.getProperty("max-players"));
    }

    public void save() throws ConfigurationSaveException {
        try (OutputStream out = Files.newOutputStream(path)) {
            properties.store(out, COMMENT);
        } catch (IOException e) {
            throw new ConfigurationSaveException("Failed to save configuration file", e);
        }
    }
}