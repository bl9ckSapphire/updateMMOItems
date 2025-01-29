package com.veronica.updatemmoitems.method.sub.get;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class GetItems {

    public static ItemStack getMMOItemsToItemStack(Player player, ItemStack targetItem){
        NBTItem nbtItem = NBTItem.get(targetItem);
        String itemType = nbtItem.getString("MMOITEMS_ITEM_TYPE");
        String itemId = nbtItem.getString("MMOITEMS_ITEM_ID");
        MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(itemType), itemId);
        return Objects.requireNonNull(mmoitem).newBuilder().build();
    }

    public static LiveMMOItem getLiveMMOItem(ItemStack targetItem) {
        // ItemStack이 null이거나 AIR인 경우 null 반환
        if (targetItem == null || targetItem.getType().isAir()) {
            return null;
        }

        // LiveMMOItem 생성자를 사용하여 변환
        try {
            return new LiveMMOItem(targetItem);
        } catch (Exception e) {
            // 예외 발생 시 (예: 아이템이 MMOItem이 아닌 경우) null 반환
            return null;
        }
    }

    public static MMOItem getMMOItem(ItemStack targetItem){
        NBTItem nbtItem = NBTItem.get(targetItem);
        String itemType = nbtItem.getString("MMOITEMS_ITEM_TYPE");
        String itemId = nbtItem.getString("MMOITEMS_ITEM_ID");
        MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(itemType), itemId);
        return mmoitem;
    }

    public static String getItemDisplayName(ItemStack paramItem) {
        String displayName;
        ItemMeta itemMeta = paramItem.getItemMeta();

        // 아이템 메타가 없거나 이름이 없는 경우
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return "이름없음";
        }

        // Component >> String 으로 변환
        Component displayComponent = itemMeta.displayName();
        displayName = PlainTextComponentSerializer.plainText().serialize(displayComponent);

        return displayName;
    }



}
