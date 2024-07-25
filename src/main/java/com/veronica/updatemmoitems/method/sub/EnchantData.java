package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.config.ConfigHandler;
import de.tr7zw.nbtapi.NBTCompound;
import net.advancedplugins.ae.api.AEAPI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.plugin.Plugin;

public class EnchantData {

    // 바닐라 인첸트와 AdvancedEnchantments 인첸트 데이터를 유지시키는 메서드

    public static ItemStack setEnchantData(ItemStack targetItem, ItemStack updatedItem) {

        Plugin aePlugin = Bukkit.getPluginManager().getPlugin("AdvancedEnchantments");

        // AdvancedEnchantments 플러그인이 존재,활성화됨과 동시에 + config 에서 "maintaining-advanced-enchantments" 설정이 true 일 경우
        if (aePlugin != null && aePlugin.isEnabled() && ConfigHandler.getInstance().getIsMaintainingAEenchant() ) {
            NBTItem nbtItem = new NBTItem(targetItem);
            if (nbtItem.hasKey("PublicBukkitValues")) {
                NBTCompound publicBukkitValues = nbtItem.getCompound("PublicBukkitValues");
                for (String key : publicBukkitValues.getKeys()) {
                    if (key.startsWith("advancedenchantments:ae_enchantment-")) {
                        String enchantName = key.substring("advancedenchantments:ae_enchantment-".length());
                        int enchantLevel = publicBukkitValues.getInteger(key);

                        AEAPI.applyEnchant(enchantName, enchantLevel, updatedItem);
                    }
                }
            }
        }


        //config 에
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
