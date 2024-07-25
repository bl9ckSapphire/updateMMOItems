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

import java.util.*;

public class CommandHandler implements CommandExecutor, TabExecutor {

    public static HashMap<String, SubCommand> subcommandList = new HashMap<>();
    private final MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (args.length == 0) {

            //============================================================================================================================================
            // 명령어의 길이가 "/업데이트" 딱 한줄일 때===================================================
            // 이 부분은 ToggleCommand 의 기능 구현부와 동일한 코드를 가져와서 씀
            if (!(sender instanceof Player player)) {
                sender.sendMessage(miniMessage.deserialize(Message.NO_PLAYER.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                return false;
            }

            // "/업데이트" 명령어 사용 펄미션이 없을 때
            if (!(sender.hasPermission(Permission.USE.getPermission()))) {
                sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                return false;
            }

            // 손에 들고있는 아이템 업데이트 메서드 수행
            Update.updateItem(player);
            return true;

            //============================================================================================================================================
        }
        String command = args[0].toLowerCase();
        if (subcommandList.containsKey(command) && sender.hasPermission(subcommandList.get(command).getPermission())) {
            subcommandList.get(command).execute(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        else {
            sender.sendMessage(miniMessage.deserialize(Message.INVALID_ARGUMENTS.getMessage(),
                    Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // "/업데이트" 명령어만 작성되었을 때
        if (args.length == 1) {
            ArrayList<String> tabCompleteCommands = new ArrayList<>(subcommandList.keySet());
            tabCompleteCommands.removeIf(cmd -> !sender.hasPermission(subcommandList.get(cmd).getPermission()));
            return tabCompleteCommands;
        }

        // "/업데이트 인벤토리" 로, "인벤토리" 가 인수로 올때
        else if (args.length == 2 && args[0].equalsIgnoreCase("인벤토리")) {
            return subcommandList.get("인벤토리").tabComplete(sender, args);
        }
        return null;
    }


}
