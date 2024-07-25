package com.veronica.updatemmoitems.permission;

public enum Permission {
    USE(new org.bukkit.permissions.Permission("updatemmoitems.command.use")),
    INVENTORY_UPDATE_COMMAND_OTHER(new org.bukkit.permissions.Permission("updatemmoitems.command.inventory.other")),
    INVENTORY_UPDATE_COMMAND_SELF(new org.bukkit.permissions.Permission("updatemmoitems.command.inventory.self")),
    RELOAD_COMMAND(new org.bukkit.permissions.Permission("updatemmoitems.command.reload"));
    final org.bukkit.permissions.Permission permission;
    Permission(org.bukkit.permissions.Permission permission) {
        this.permission = permission;
    }
    public org.bukkit.permissions.Permission getPermission() {
        return this.permission;
    }
}