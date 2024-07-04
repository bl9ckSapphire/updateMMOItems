package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheckMainHand {

    public static boolean chechMainHand(Player player){

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        // 메인핸드에 아이템을 들고 있는지 확인
        if ((itemInHand == null || itemInHand.getType().isAir() || itemInHand.getAmount() == 0)) {
            return false;
        }
        else { return true; }
    }
}
