package me.rochblondiaux.limbo.network.protocol.model;

@FunctionalInterface
public interface MetadataWriter {

    void writeData(ByteMessage message, Version version);

}