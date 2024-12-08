package me.rochblondiaux.limbo.connection;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Unmodifiable;

import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.network.connection.ClientConnection;

@Log4j2
public class ConnectionManager {

    private final Map<UUID, ClientConnection> connections = new ConcurrentHashMap<>();

    public void register(ClientConnection connection) {
        this.connections.put(connection.uniqueId(), connection);
        log.info("Player {} connected ({}) [{}]", connection.profile().username(), connection.address(), connection.version());
    }

    public void unregister(ClientConnection connection) {
        this.connections.remove(connection.uniqueId());
        log.info("Player {} disconnected ({})", connection.profile().username(), connection.address());
    }

    public int count() {
        return this.connections.size();
    }

    @Unmodifiable
    public Collection<ClientConnection> connections() {
        return Collections.unmodifiableCollection(this.connections.values());
    }


}
