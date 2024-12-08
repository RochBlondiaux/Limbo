package me.rochblondiaux.limbo.permissions;

import java.util.Set;

import org.jetbrains.annotations.Unmodifiable;

public interface PermissionHolder {

    void addPermission(String permission);

    void removePermission(String permission);

    boolean hasPermission(String permission);

    void clearPermissions();

    @Unmodifiable
    Set<String> permissions();
}
