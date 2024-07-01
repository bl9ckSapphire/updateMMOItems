package com.veronica.updatemmoitems.method;

import de.tr7zw.nbtapi.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.inventory.ItemStack;

public class Get {

    // 손에 들고 있는 MMOItems 정보를 가져오는 메서드
    public static MMOItem getMMOItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);


        // 메인핸드에 아이템을 들고 있는지 확인(2)
        // 혹시나 모를 결함을 대비하여, getMMOItem 메서드 내부에서도 빈 손인지 확인.
        if (itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() == 0) {
            return null;
        }

        // MMOItems 의 기본적인 타입과 ID 태그가 없을 시, null 반환
        if (!nbtItem.hasKey("MMOITEMS_ITEM_TYPE") || !nbtItem.hasKey("MMOITEMS_ITEM_ID")) {
            return null;
        }

        String itemType = nbtItem.getString("MMOITEMS_ITEM_TYPE");
        String itemId = nbtItem.getString("MMOITEMS_ITEM_ID");

        Type type = MMOItems.plugin.getTypes().get(itemType);
        if (type == null) {
            return null;
        }

        return MMOItems.plugin.getMMOItem(type, itemId);
    }
}
