package me.rochblondiaux.limbo.network.protocol.model;

public interface ClientboundPacket extends Packet {

    void encode(ByteMessage message, Version version);

}
