package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.OtherPluginHandler;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CheckLatest {

    public static boolean isLatestCustomItems(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) {
            return false;
        }

        if (item1.getType() != item2.getType()) {
            return false;
        }

        NBTItem nbtItem1 = new NBTItem(item1);
        NBTItem nbtItem2 = new NBTItem(item2);

        //특정 nbt 태그 제거 후 비교
        removeSpecificNBTTags(nbtItem1);
        removeSpecificNBTTags(nbtItem2);

        if (!nbtItem1.toString().equals(nbtItem2.toString())) {
            return false;
        }

        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        if (meta1 == null || meta2 == null) {
            return meta1 == meta2;
        }

        NBTItem metaNBTItem1 = new NBTItem(new ItemStack(item1.getType()));
        NBTItem metaNBTItem2 = new NBTItem(new ItemStack(item2.getType()));

        metaNBTItem1.mergeCompound(nbtItem1);
        metaNBTItem2.mergeCompound(nbtItem2);

        //특정 nbt 태그 제거 후 비교
        removeSpecificNBTTags(metaNBTItem1);
        removeSpecificNBTTags(metaNBTItem2);

        return metaNBTItem1.toString().equals(metaNBTItem2.toString());
    }

    private static void removeSpecificNBTTags(NBTItem nbtItem) {
        if (ConfigHandler.getInstance().getIsMaintainingVanillaEnchantment()) {
            nbtItem.removeKey("Enchantments");
            nbtItem.removeKey("RepairCost");
        }

        // PublicBukkitValues 제거
        // 2024-07-17 현재 이 기능은 작동이 안되고 있음. "advancedenchantments:slots" 을 인식하지 못하는 것 같음
        // 따라서, 아이템에 AE 인첸트를 빼고 남은 찌거기 nbt 인 "PublicBukkitValues:{"advancedenchantments:slots":0}"
        // 딱 이 부분만을 없애주기 위해 한번 더 업데이트 되는 현상은 있음
        //NBTCompound publicBukkitValues = nbtItem.getCompound("PublicBukkitValues");

        // PublicBukkitValues 태그와 그 안의 모든 값 제거 (AE 인첸트와 관련된 태그를 배제하고 비교하기 위함)
        if (nbtItem.hasTag("PublicBukkitValues")) {
            nbtItem.removeKey("PublicBukkitValues");
        }



        // Lore 값 제거 (AE 인첸트의 인첸트 표시는 로어에 표시되므로, 로어도 모두 배제시키고 비교)
        // 기본적으로 MMOItems 에서 설정한 로어는, "MMOITEMS_LORE:" 라는 태그에 저장됨. 즉 바닐라 Lore 태그안에 저장되는 식이 아니라서
        // Lore 값을 제거하더라도, MMOItems 에서 로어를 추가했다면 MMOITEMS_LORE 값이 바뀌기 때문에 업데이트가 수행됨
        ReadWriteNBT displayTag = nbtItem.getCompound("display");
        // 1.20.5+ 부터 ItemStacks는 런타임 동안 vanilla nbt를 더 이상 갖지 않아서
        // Lore, display 태그를 제거하는 것은 1.20.4 이하 버전을 위한 코드
        // (20.5+ 부터는 바닐라태그인 Lore, display 가 nbtItem 에 담기지 않아서 해당 코드는 필요없음)
        if (displayTag != null) {
            displayTag.removeKey("Lore");
            // display 태그가 비어 있으면 제거
            if (displayTag.getKeys().isEmpty()) {
                nbtItem.removeKey("display");
            }
        }

    }
}