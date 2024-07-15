package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import org.bukkit.inventory.ItemStack;
import de.tr7zw.nbtapi.NBTItem;

import java.util.List;

public class Whitelist {


    public static boolean whitelistCheck(ItemStack targetItem) {

        // 화이트리스트 옵션이 false 일 경우 (사용안함. 꺼져있을 경우)
        if (!ConfigHandler.getInstance().getIsWorkWhitelist()) { return true; }

        NBTItem targetItemNBT = new NBTItem(targetItem);
        String itemType = targetItemNBT.getString("MMOITEMS_ITEM_TYPE");
        String itemID = targetItemNBT.getString("MMOITEMS_ITEM_ID");

        // ID 또는 Type 이 없는 아이템인 경우 false 반환
        if (itemType == null || itemID == null ) { return false; }

        List<String> typeList = ConfigHandler.getInstance().getWhitelistTypeList();

        for (String value : typeList) {
            String[] parts = value.split(":");
            if (parts.length != 2) {
                UpdateMMOItems.getPluginLogger().warning(value + "는, 잘못된 whitelist 표기입니다.");
                continue;
            }

            String whitelistedType = parts[0];
            String whitelistedId = parts[1];

            if (itemType.equals(whitelistedType)) {
                // whitelistedId가 "*"이거나 itemID와 일치하면 true 반환
                if (whitelistedId.equals("*") || whitelistedId.equals(itemID)) {
                    return true;
                }
            }
        }

        // 일치하는 항목을 찾지 못한 경우 false
        return false;
    }
}