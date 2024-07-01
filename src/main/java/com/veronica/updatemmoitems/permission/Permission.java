package com.veronica.updatemmoitems.permission;

public enum Permission {
    USE(new org.bukkit.permissions.Permission("updatemmoitems.use")),
    RELOAD_COMMAND(new org.bukkit.permissions.Permission("updatemmoitems.reload"));
    final org.bukkit.permissions.Permission permission;
    Permission(org.bukkit.permissions.Permission permission) {
        this.permission = permission;
    }
    public org.bukkit.permissions.Permission getPermission() {
        return this.permission;
    }
}