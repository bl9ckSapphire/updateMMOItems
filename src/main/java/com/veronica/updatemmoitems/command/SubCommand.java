package com.veronica.updatemmoitems.command;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.List;

public abstract class SubCommand {
    private final Permission permission;

    public SubCommand(Permission permission) {
        this.permission = permission;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public Permission getPermission() {
        return permission;
    }

    public abstract List<String> tabComplete(CommandSender sender, String[] args);

}