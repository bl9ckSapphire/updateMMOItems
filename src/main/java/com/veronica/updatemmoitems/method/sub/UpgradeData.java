package com.veronica.updatemmoitems.method.sub;

import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.inventory.ItemStack;
import net.Indyuce.mmoitems.stat.data.type.StatData;

public class UpgradeData {

    public static ItemStack setUpgradeData(MMOItem mmoItem, ItemStack updatedItem) {
        if (mmoItem.hasUpgradeTemplate()) {
            MMOItem targetMMOItem = MMOItems.plugin.getMMOItem(mmoItem.getType(), mmoItem.getId());
            if (targetMMOItem != null) {
                // 새로운 MMOItem 객체 생성
                MMOItem updatedMMOItem = new MMOItem(mmoItem.getType(), mmoItem.getId());

                // 업그레이드 레벨 설정
                StatData upgradeData = targetMMOItem.getData(ItemStats.UPGRADE);
                if (upgradeData instanceof net.Indyuce.mmoitems.stat.data.UpgradeData) {
                    updatedMMOItem.setData(ItemStats.UPGRADE, upgradeData);
                }

                // 현재 아이템의 업그레이드 정보까지 담아서 새로운 ItemStack 생성
                return updatedMMOItem.newBuilder().build();
            }
        }
        return updatedItem;
    }
}