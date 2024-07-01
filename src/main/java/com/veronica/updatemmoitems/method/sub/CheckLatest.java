package com.veronica.updatemmoitems.method.sub;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CheckLatest {

    // 아이템 정보를 비교하는 메서드
    // 즉, 해당 아이템이 이미 최신화된 상태인지를 비교
    public static boolean isLatestMMOItems(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) {
            return false;
        }

        // 아이템의 종류(Material) 비교
        if (item1.getType() != item2.getType()) {
            return false;
        }

        // 아이템의 NBT 데이터 비교
        NBTItem nbtItem1 = new NBTItem(item1);
        NBTItem nbtItem2 = new NBTItem(item2);

        if (!nbtItem1.toString().equals(nbtItem2.toString())) {
            return false;
        }

        // 아이템의 메타 데이터 비교
        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        if (meta1 == null || meta2 == null) {
            return meta1 == meta2;
        }

        return meta1.equals(meta2);
    }
}
