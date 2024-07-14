package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.tr7zw.nbtapi.NBTItem;

import java.util.List;

public class Whitelist {

    private static final MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();

    public static boolean whitelistCheck(ItemStack targetItem, Player player){

        // 화이트리스트 옵션이 false 일 경우 (사용안함. 꺼져있을 경우)
        if(!ConfigHandler.getInstance().getIsWorkWhitelist()){ return true; }

        // 아이템의 NBT 데이터를 변수로 가져옴
        NBTItem targetItemNBT = new NBTItem(targetItem);

        // itemType 변수에, 해당 아이템의 "MMOITEMS_ITEM_TYPE" NBT 태그의 키 값을 대입 (BOW, SWORD 등의 값들이 대입될것임)
        String itemType = targetItemNBT.getString("MMOITEMS_ITEM_TYPE");

        // typeList 변수에, config 에 작성된 화이트리스트 type 들의 값들을 가져옴 (배열변수)
        List<String> typeList = ConfigHandler.getInstance().getWhitelistTypeList();

        // itemType 의 "MMOITEMS_ITEM_TYPE" NBT 태그의 키 값이 null 이거나,
        // typeList 의 배열에 저장된 값들과 itemType의 값을 비교하여, 일치하는 값이 없을 때 false 반환
        if (itemType == null || !typeList.contains(itemType)) { return false; }

        // typeList 의 배열에 저장된 값들과 itemType의 값을 비교하여, 일치하는 값이 있을 때
        // 즉 화이트리스트에 허용된 type 을 가진 아이템이라면 true 반환
        else { return true; }

    }
}
