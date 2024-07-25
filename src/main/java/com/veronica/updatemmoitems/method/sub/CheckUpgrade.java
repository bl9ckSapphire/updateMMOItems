package com.veronica.updatemmoitems.method.sub;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static org.bukkit.Bukkit.getServer;

public class CheckUpgrade {

    public static boolean isUpgradingItems(ItemStack item) {
        if (item == null) { return false; }
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasKey("MMOITEMS_UPGRADE")) {
            String upgradeInfo = nbtItem.getString("MMOITEMS_UPGRADE");
            try {
                JSONParser parser = new JSONParser();
                JSONObject upgradeJson = (JSONObject) parser.parse(upgradeInfo);

                if (upgradeJson.containsKey("Level")) {
                    int level = ((Long) upgradeJson.get("Level")).intValue();
                    // 디버깅용 로그출력
                    // getServer().getConsoleSender().sendMessage("[updateMMOItems] " + ChatColor.YELLOW + "해당 아이템의 업그레이드 레벨: " + level);
                    return level > 0;
                }
            }
            catch (ParseException e) { getServer().getConsoleSender().sendMessage("[updateMMOItems] " + ChatColor.RED + "JSON 파싱 오류: " + e.getMessage()); }
        }
        return false;
    }
}