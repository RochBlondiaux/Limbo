package me.rochblondiaux.limbo.network.protocol.packets.status.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rochblondiaux.limbo.Limbo;
import me.rochblondiaux.limbo.configuration.ServerConfiguration;
import me.rochblondiaux.limbo.network.protocol.model.ByteMessage;
import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class ClientboundStatusResponsePacket implements ClientboundPacket {

    private static final String TEMPLATE = "{ \"version\": { \"name\": \"%s\", \"protocol\": %d }, \"players\": { \"max\": %d, \"online\": %d, \"sample\": [] }, \"description\": %s, \"enforcesSecureChat\": false }";
    private static final GsonComponentSerializer GSON_COMPONENT_SERIALIZER = GsonComponentSerializer.gson();
    private Limbo app;

    @Override
    public void encode(ByteMessage message, Version version) {
        ServerConfiguration configuration = app.configuration();

        int protocol;
        int staticProtocol = configuration.motdProtocol();

        if (staticProtocol > 0) {
            protocol = staticProtocol;
        } else {
            protocol = version.protocolNumber();
        }

        String jsonResponse = getResponseJson(
                Version.of(protocol).prettyName(),
                protocol,
                configuration.maxPlayers(),
                app.server().connections().count(),
                configuration.motd()
        );
        log.debug("Sending status response: {}", jsonResponse);
        message.writeString(jsonResponse);
    }

    private String getResponseJson(String version, int protocol, int maxPlayers, int online, Component description) {
        String gsonDescription = GSON_COMPONENT_SERIALIZER.serialize(description);
        log.debug("Gson description: {}", gsonDescription);
        return String.format(TEMPLATE, version, protocol, maxPlayers, online, gsonDescription);
    }
}
