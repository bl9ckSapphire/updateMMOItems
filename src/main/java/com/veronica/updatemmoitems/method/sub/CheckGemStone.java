package com.veronica.updatemmoitems.method.sub;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class CheckGemStone {

    public static boolean isInGemstone(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        // MMOITEMS_GEM_STONES nbt 태그 존재여부 확인
        if (nbtItem.hasKey("MMOITEMS_GEM_STONES")) {
            String gemStonesJson = nbtItem.getString("MMOITEMS_GEM_STONES");

            // JSON 파싱
            Gson gson = new Gson();
            JsonObject gemStonesObject = gson.fromJson(gemStonesJson, JsonObject.class);

            // Gemstones 배열 ("Gemstones":[]) 이 존재하고 비어있지 않은지 확인
            if (gemStonesObject.has("Gemstones")) {
                JsonArray gemstonesArray = gemStonesObject.getAsJsonArray("Gemstones");

                // 해당 배열에 요소가 존재(size가 1 이상이면) true 반환
                return gemstonesArray.size() > 0;
            }
        }

        // MMOITEMS_GEM_STONES 태그가 없거나 Gemstones 배열이 없으면 false 반환
        return false;
    }
}