package com.veronica.updatemmoitems.command.sub;

import com.veronica.updatemmoitems.command.SubCommand;
import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.method.InvUpdate;
import com.veronica.updatemmoitems.method.sub.PlaySounds;
import com.veronica.updatemmoitems.permission.Permission;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class InvUpdateCommand extends SubCommand {
    private final MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();
    public InvUpdateCommand() {
        super(Permission.INVENTORY_UPDATE_COMMAND_SELF.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(miniMessage.deserialize(Message.NO_PLAYER.getMessage(),
                    Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
            return;
        }


        // "/업데이트 인벤토리" 명령어로 arg 길이가 1 일 때
        if (args.length == 0) {

            // 본인의 인벤토리 아이템 업데이트 펄미션이 없을 때
            if (!sender.hasPermission(Permission.INVENTORY_UPDATE_COMMAND_SELF.getPermission()) ) {

                sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage(),
                        Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

                // 실패 사운드 재생
                PlaySounds.playSounds((Player) sender, ConfigHandler.getInstance().getFailSounds());

                return;
            }

            // 사용자 본인의 인벤토리 아이템 업데이트 수행
            InvUpdate.allInventoryUpdate((Player) sender, null, null);
        }

        // "/업데이트 인벤토리 bl9ckSapphire" 명령어로 arg 길이가 2일 때
        else if (args.length == 1) {

            // 명령어 사용자가 타인의 인벤토리 아이템 업데이트 펄미션이 없을 때
            if (!sender.hasPermission(Permission.INVENTORY_UPDATE_COMMAND_OTHER.getPermission())) {

                // NO_PERMISSION 대신 INVALID_ARGUMENTS 를 써서, "권한이 없습니다.." 대신 "사용법 : /업데이트 또는 /업데이트 인벤토리"
                // 메시지를 보내서, 권한이 없는 유저가 타인의 인벤토리 업데이트 기능 자체가 없는 것처럼 보이게 하기위함
                // 따라서 이곳에서는 실패 사운드조차 보내지 않아, 권한이 없는 일반 유저들은, 완전히 없는 명령어처럼 인지하게 하기위함
                sender.sendMessage(miniMessage.deserialize(Message.INVALID_ARGUMENTS.getMessage(),
                        Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

                return;
            }


            String targetPlayerName = args[0];
            Player targetPlayer = sender.getServer().getPlayer(targetPlayerName);

            // 인수로 받은 유저의 닉네임이 없거나 온라인상태가 아닌 유저일 때
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                sender.sendMessage(miniMessage.deserialize(Message.INVALID_PLAYER.getMessage(),
                        Placeholder.parsed("prefix", Message.PREFIX.getMessage()),
                        Placeholder.parsed("player", targetPlayerName)));

                // 실패 사운드 재생
                PlaySounds.playSounds((Player) sender, ConfigHandler.getInstance().getFailSounds());
                return;
            }

            // 해당 유저의 인벤토리 아이템 업데이트 수행
            InvUpdate.allInventoryUpdate(targetPlayer, (Player) sender, null);

        }
        else {

            sender.sendMessage(miniMessage.deserialize(Message.INVALID_ARGUMENTS.getMessage(),
                    Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
        }
    }


    // Tab completion for <플레이어>
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        // args 길이가2 임과 동시에, updatemmoitems.command.inventory.other 펄미션 (타인의 인벤토리 업데이트 권한) 보유 중일 때
        // 플레이어 닉네임 탭 자동완성 리스트를 띄움
        if (args.length == 2 && sender.hasPermission(Permission.INVENTORY_UPDATE_COMMAND_OTHER.getPermission())) {
            String search = args[1].toLowerCase();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(search)) {
                    completions.add(player.getName());
                }
            }
        }
        return completions;
    }
}
