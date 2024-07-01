package com.veronica.updatemmoitems.command;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.method.Update;
import com.veronica.updatemmoitems.permission.Permission;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler implements CommandExecutor, TabExecutor {


    private final Map<String, SubCommand> subcommandList = new HashMap<>();
    private final MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();

    public void registerSubCommand(String commandName, SubCommand subCommand) {
        subcommandList.put(commandName.toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(miniMessage.deserialize(Message.NO_PLAYER.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                return true;
            }

            Player player = (Player) sender;

            if (player.hasPermission(Permission.USE.getPermission())){
                Update.updateItem(player);
            }
            else {
                sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                return true;
            }

        }
        else {
            // 만약 "/업데이트" 처럼, args 가 0 이 아닌 경우 수행되는 곳
            executeCommand(sender, args[0], args);
        }
        return true;
    }

    private void executeCommand(CommandSender sender, String command, String[] args) {
        SubCommand subCommand = subcommandList.get(command.toLowerCase());
        if (subCommand != null && sender.hasPermission(subCommand.getPermission())) {
            subCommand.execute(sender, args.length > 1 ? args : new String[0]);
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> tabCompleteCommands = new ArrayList<>();
            for (String cmd : subcommandList.keySet()) {
                if (sender.hasPermission(subcommandList.get(cmd).getPermission())) {
                    tabCompleteCommands.add(cmd);
                }
            }
            return tabCompleteCommands;
        }
        return null;
    }
}
