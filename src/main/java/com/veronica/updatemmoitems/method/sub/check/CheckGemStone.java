package com.veronica.updatemmoitems.method.sub.check;

import com.veronica.updatemmoitems.method.sub.get.GetItems;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheckGemStone {

    public static boolean isInGemstone(Player player, ItemStack itemStack) {

        LiveMMOItem targetLiveMMOItem = GetItems.getLiveMMOItem(itemStack);

        // 젬스톤 슬롯이 비었을 때
        if (targetLiveMMOItem.getGemStones().isEmpty()) { return false; }

        // 비어있이 않을 때
        return true;


    }
}