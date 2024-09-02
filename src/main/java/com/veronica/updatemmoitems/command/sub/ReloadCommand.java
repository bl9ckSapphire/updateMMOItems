package com.veronica.updatemmoitems.command.sub;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.command.SubCommand;
import com.veronica.updatemmoitems.config.AliasesHandler;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.config.OtherPluginHandler;
import com.veronica.updatemmoitems.permission.Permission;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SubCommand {
    MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();
    public ReloadCommand() {
        super(Permission.RELOAD_COMMAND.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission(Permission.RELOAD_COMMAND.getPermission())) {

            // config.yml 리로드
            UpdateMMOItems.getInstance().reloadConfig();
            ConfigHandler.getInstance().reloadConfigOptions();

            // aliases.yml 리로드 및 적용
            AliasesHandler.getInstance().reloadAliasesConfig();

            // otherPlugins.yml 리로드 및 적용
            OtherPluginHandler.getInstance().reloadOtherPluginsConfig();


            sender.sendMessage(miniMessage.deserialize(Message.RELOAD.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
    }



    // ReloadCommand 에서는 플레이어 이름에 대한 탭 자동완성이 필요하지 않으므로 빈 리스트 반환
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
