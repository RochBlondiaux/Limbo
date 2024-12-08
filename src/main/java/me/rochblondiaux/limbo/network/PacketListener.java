package me.rochblondiaux.limbo.network;

import me.rochblondiaux.limbo.network.protocol.model.ClientboundPacket;
import me.rochblondiaux.limbo.network.protocol.model.ServerboundPacket;

public interface PacketListener {

    void onPacketReceived(ServerboundPacket packet);

    void onPacketSent(ClientboundPacket packet);
}
