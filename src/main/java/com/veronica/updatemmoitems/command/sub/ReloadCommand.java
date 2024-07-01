package com.veronica.updatemmoitems.command.sub;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.command.SubCommand;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.permission.Permission;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {
    MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();
    public ReloadCommand() {
        super(Permission.RELOAD_COMMAND.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission(Permission.RELOAD_COMMAND.getPermission())) {

            UpdateMMOItems.getInstance().reloadConfig();
            ConfigHandler.getInstance().reloadConfigOptions();
            sender.sendMessage(miniMessage.deserialize(Message.RELOAD.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
    }
}
