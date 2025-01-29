package com.veronica.updatemmoitems.method.sub.get;

import com.veronica.updatemmoitems.method.sub.set.SetGemstone;
import com.veronica.updatemmoitems.method.sub.set.SetUpgrade;
import de.tr7zw.nbtapi.NBT;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.inventory.ItemStack;


public class GetLatestItems {

    // 손에 들고 있는 아이템을 가장 최신 정보로 가져오는 메서드
    public static ItemStack getLatestCustomItems(ItemStack targetItem) {


        // NBT 데이터 확인
        return NBT.get(targetItem, nbt -> {

            if (nbt.hasTag("MMOITEMS_ITEM_TYPE") && nbt.hasTag("MMOITEMS_ITEM_ID")) {

                String itemType = nbt.getString("MMOITEMS_ITEM_TYPE");
                String itemId = nbt.getString("MMOITEMS_ITEM_ID");
                Type type = MMOItems.plugin.getTypes().get(itemType);
                ItemStack mi = MMOItems.plugin.getItem(type, itemId);

                // 위의 Type 과 ID 를 지닌 MMOItems 가 없을경우 null반환
                if (mi == null) { return null; }


                // 타깃 아이템을 LiveMMOItem 자료형인 targetMMOItems 으로 선언
                LiveMMOItem targetLiveMMOItems = GetItems.getLiveMMOItem(targetItem);
                // ============================================================================
                // MMOItem 으로 선언하면, 서버에 등록된 항상 최신의 데이터를 가져오기 때문에
                // 매개변수로 받은 아이템만의 독자적인 아이템 데이터를 가져오기 위해서 LiveMMOItem 사용
                // 결국은, 이 독자적인 아이템 데이터와, 서버에 등록된 최신 아이템의 데이터를 비교하기 때문
                // (궁극적으론, 이 독자적인 아이템 데이터가 최신 데이터와 동일한지 비교하고 업데이트 진행)
                // ============================================================================


                // targetItem 과 Type,ID 가 동일한 최신 데이터의 latestMMOItem 선언
                MMOItem latestMMOItem = GetItems.getMMOItem(targetItem);




                // 업그레이드 데이터 적용
                ItemStack latestMMOItemsToItemStack = SetUpgrade.setUpgradeData(targetLiveMMOItems, latestMMOItem);

                if (latestMMOItemsToItemStack == null) {
                    // latestMMOItemsToItemStack 이 null 일 때 == 업그레이드 정보가 없는 MMOItems 일 때
                    // targetItem 과 Type,ID 가 동일한 최신 데이터의 latestMMOItem 를 그대로 반환
                    latestMMOItemsToItemStack = latestMMOItem.newBuilder().build();
                }

                // Gemstone 데이터 적용
                LiveMMOItem latestMMOItemsToLiveMMOItem = new LiveMMOItem(latestMMOItemsToItemStack);
                latestMMOItemsToItemStack = SetGemstone.setGemstoneData(targetLiveMMOItems, latestMMOItemsToLiveMMOItem);

                if (latestMMOItemsToItemStack == null) { latestMMOItemsToItemStack = latestMMOItem.newBuilder().build(); }

                return latestMMOItemsToItemStack;


            }
            return null; // 조건에 맞는 최신 정보가 없으면 null 반환
        });
    }
}
