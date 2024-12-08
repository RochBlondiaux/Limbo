package me.rochblondiaux.limbo.command;

import me.rochblondiaux.limbo.permissions.PermissionHolder;
import net.kyori.adventure.audience.Audience;

public interface CommandSender extends Audience, PermissionHolder {

    String getName();
}
