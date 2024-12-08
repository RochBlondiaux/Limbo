package me.rochblondiaux.limbo.network.protocol.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.nbt.BinaryTag;

@Setter
@Getter
@RequiredArgsConstructor
public class NbtMessage {

    private String json;
    private BinaryTag tag;

}
