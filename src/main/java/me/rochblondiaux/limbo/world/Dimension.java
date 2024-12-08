package me.rochblondiaux.limbo.world;

import net.kyori.adventure.nbt.CompoundBinaryTag;

public record Dimension(int id, String name, CompoundBinaryTag data) {

}