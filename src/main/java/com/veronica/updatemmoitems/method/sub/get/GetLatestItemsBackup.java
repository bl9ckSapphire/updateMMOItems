package com.veronica.updatemmoitems.method.sub.get;


import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.inventory.ItemStack;
/*
public class GetLatestItemsBackup {


    // 손에 들고 있는 아이템(IA, Oraxen, MMOItems 인 경우)을 가장 최신 정보로 가져오는 메서드
    public static ItemStack getLatestCustomItems(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        ItemStack latestTargetItems = null;


        // 메인핸드에 아이템을 들고 있는지 확인(2)
        // 혹시나 모를 결함을 대비하여, getMMOItem 메서드 내부에서도 빈 손인지 확인.
        if (itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() == 0) {
            return null;
        }



        // Oraxen 태그가 있는지 확인 (oraxen:id 키는 PublicBukkitValues 태그안에 들어있음)
        // + otherPlugin.yml 에서 Oraxen 아이템을 업데이트 할 건지 여부가 false (Oraxen 아이템 정보가 있는 MMOItems 는 업데이트 시키지 않음)
        if (nbtItem.hasKey("PublicBukkitValues") && !OtherPluginHandler.getInstance().getIsEnableOraxenItems()) {
            NBTCompound publicBukkitValues = nbtItem.getCompound("PublicBukkitValues");
            if (publicBukkitValues.hasKey("oraxen:id")) {
                return null;
            } // null 을 반환하여 업데이트시키지 않도록
        }

        // ItemsAdder 태그가 있는지 확인
        // + otherPlugin.yml 에서 ItemsAdder 아이템을 업데이트 할 건지 여부가 false 일 때
        if (nbtItem.hasKey("itemsadder") && !OtherPluginHandler.getInstance().getIsEnableItemsAdderItems()) {
            return null; // null 을 반환하여 업데이트시키지 않도록
        }

        // oraxen 태그도 없고, ItemsAdder 태그도 없으면서, MMOItems의 Type, ID 태그 보유 시 (MMOItems 의 정보를 지닌 oraxen 또는 ia 아이템이 아닌, 일반적인 MMOItems 인 경우)
        if ( nbtItem.hasKey("MMOITEMS_ITEM_TYPE") && nbtItem.hasKey("MMOITEMS_ITEM_ID") ) {


            String itemType = nbtItem.getString("MMOITEMS_ITEM_TYPE");
            String itemId = nbtItem.getString("MMOITEMS_ITEM_ID");

            Type type = MMOItems.plugin.getTypes().get(itemType);
            if (type == null) { return null; }


            MMOItem mmoItem = MMOItems.plugin.getMMOItem(type, itemId);

            // updatedItem 변수에 해당 Type, ID 를 지닌 아이템 정보를 받아옴(즉, 해당 아이템의 가장 현재 정보를 받아옴)
            latestTargetItems = mmoItem.newBuilder().build();


            // 추후에 추가할 기능 (현재는 사용x)
            // 매개변수로 받은 손에 들고있는 아이템이, 업그레이드 레벨이 존재한다면 (업그레이드가 1강 이상, 또는 업그레이드 가능한 아이템일 때)
            if ( GetUpgradeLevel.getUpgradeLevel(nbtItem.getItem()) > 0 ){

                // 매개변수로 받은 아이템의 업그레이드 레벨 값을 originItemHaveLevel 변수에 대입
                int originItemHaveLevel = GetUpgradeLevel.getUpgradeLevel(nbtItem.getItem());

                // 최종적으로 반환할 MMOItems 아이템에, originItemHaveLevel 값 만큼의 업그레이드를 적용시킴
            }


            return latestTargetItems;
        }
        return latestTargetItems;
    }
}
*/
