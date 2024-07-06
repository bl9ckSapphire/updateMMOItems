package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.config.ConfigHandler;
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

        // 아이템 업데이트 수행 시, 바닐라 인첸트 정보를 유지할건지 판단하는
        // config 의 "maintaining-vanilla-enchantment-data:" 옵션이 켜져있다면 수행
        if (ConfigHandler.getInstance().getIsMaintainingVanillaEnchantment()){

            // 여기서는 바닐라 인첸트 시 붙는 태그 (Enchantments, RepairCost) 를 제거하여 비교
            nbtItem1.removeKey("Enchantments");
            nbtItem1.removeKey("RepairCost");
            nbtItem2.removeKey("Enchantments");
            nbtItem2.removeKey("RepairCost");
        }

        // 제거된 후에 NBT 데이터 비교
        if (!nbtItem1.toString().equals(nbtItem2.toString())) {
            return false;
        }

        // 아이템의 메타 데이터 비교
        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        if (meta1 == null || meta2 == null) {
            return meta1 == meta2;
        }

        // 바닐라 인첸트 태그를 제외한 NBT 데이터로 비교
        NBTItem metaNBTItem1 = new NBTItem(new ItemStack(item1.getType()));
        NBTItem metaNBTItem2 = new NBTItem(new ItemStack(item2.getType()));

        metaNBTItem1.mergeCompound(nbtItem1);
        metaNBTItem2.mergeCompound(nbtItem2);

        metaNBTItem1.removeKey("Enchantments");
        metaNBTItem1.removeKey("RepairCost");

        metaNBTItem2.removeKey("Enchantments");
        metaNBTItem2.removeKey("RepairCost");

        // 메타 NBT 데이터 비교
        return metaNBTItem1.toString().equals(metaNBTItem2.toString());
    }
}
