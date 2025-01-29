package com.veronica.updatemmoitems.method.sub.check;

import com.nexomc.nexo.api.NexoItems;
import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.method.sub.SendMessage;
import dev.lone.itemsadder.api.ItemsAdder;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class CheckOtherPluginItem {

    public static boolean isOtherPluginItems(Player player, ItemStack targetItem){

        Plugin nexoPlugin = getServer().getPluginManager().getPlugin("Nexo");
        Plugin oraxenPlugin = getServer().getPluginManager().getPlugin("Oraxen");
        Plugin itemsAdderPlugin = getServer().getPluginManager().getPlugin("ItemsAdder");

        // 서버에 nexo 플러그인이 존재+활성화 상태이면서 >> 타깃 아이템이 Nexo 아이템일 경우
        if (nexoPlugin != null && nexoPlugin.isEnabled() && NexoItems.exists(targetItem)){
            SendMessage.DebugLog(player ,"Nexo 아이템임");
            return true;
        }

        if (oraxenPlugin != null && oraxenPlugin.isEnabled() && OraxenItems.exists(targetItem)){
            SendMessage.DebugLog(player ,"Oraxen 아이템임");
            return true;
        }

        if (itemsAdderPlugin != null && itemsAdderPlugin.isEnabled() && ItemsAdder.isCustomItem(targetItem)){
            SendMessage.DebugLog(player ,"ItemsAdder 아이템임");
            return true;
        }

        return false;

    }
}
