package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.config.ConfigHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class EnchantData {

    public static ItemStack setEnchantData(ItemStack targetItem, ItemStack updatedItem) {
        if (ConfigHandler.getInstance().getIsMaintainingVanillaEnchantment()) {
            // 인첸트 데이터 유지하기
            if (targetItem.hasItemMeta() && targetItem.getItemMeta().hasEnchants()) {
                updatedItem.addUnsafeEnchantments(targetItem.getEnchantments());
            }

            // RepairCost 데이터 유지하기
            if (targetItem.hasItemMeta() && targetItem.getItemMeta() instanceof Repairable) {
                Repairable targetRepairable = (Repairable) targetItem.getItemMeta();

                // updatedItem의 ItemMeta가 Repairable인지 확인
                if (updatedItem.hasItemMeta() && updatedItem.getItemMeta() instanceof Repairable) {
                    Repairable updatedRepairable = (Repairable) updatedItem.getItemMeta();
                    updatedRepairable.setRepairCost(targetRepairable.getRepairCost());

                    // RepairCost 값을 updatedItem의 ItemMeta에 설정
                    updatedItem.setItemMeta((ItemMeta) updatedRepairable);
                }
            }
        }
        return updatedItem;
    }

}
