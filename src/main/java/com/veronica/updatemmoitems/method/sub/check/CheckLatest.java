package com.veronica.updatemmoitems.method.sub.check;

import com.veronica.updatemmoitems.config.IgnoreNbtHandler;
import com.veronica.updatemmoitems.method.sub.SendMessage;
import com.veronica.updatemmoitems.method.sub.get.GetItems;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.stat.data.*;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.Indyuce.mmoitems.stat.data.EnchantListData;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;


public class CheckLatest {


    public static boolean isLatestCustomItems(Player player, ItemStack targetItem, ItemStack latestCustomItem) {


        if (targetItem == null || latestCustomItem == null) { return false; }
        if (targetItem.getType() != latestCustomItem.getType()) { return false; }



        /**
         * 수량을 무시한 아이템으로 변환 (두 아이템 수량을 모두 1로 설정하고, 서로의 NBT 태그를 비교)
         * 이렇게 하는 이유는, 아이템의 겹쳐진 수량은 NBT 태그에 나타나지도 않을 뿐더러, 수량이 1개 이상의 겹쳐진 아이템을 들고 업데이트를 진행하는 경우
         * 무한정 업데이트가 되버림. 이는 겹쳐진 아이템과 1개의 아이템의 NBT 가 서로 다르다는 뜻이지만,
         * 수량을 의미하는 NBT 태그가 존재하지 않아서, 처음 비교할때부터 수량을 1로 두고 두 아이템을 비교하기 위함
         */

        targetItem = ignoreAmount(targetItem);
        latestCustomItem = ignoreAmount(latestCustomItem);

        ReadWriteNBT holdMainHandNBT = NBT.itemStackToNBT(targetItem);
        ReadWriteNBT latestDateNBT = NBT.itemStackToNBT(latestCustomItem);

        // 제외할 태그 삭제 (( 1.20.5+ 부터는 해당 메서드를 사용해, 비교에서 제외할 NBT 태그를 제거 components))
        removeComponentsNBT(player, holdMainHandNBT);
        removeComponentsNBT(player, latestDateNBT);


        // 단순 문자열 비교
        String nbt1 = holdMainHandNBT.toString();
        String nbt2 = latestDateNBT.toString();

        boolean isEqual = nbt1.equals(nbt2);
        if (isEqual) {
            SendMessage.DebugLog(player, "손에 들고있는 아이템과 최신 MI 아이템이 동일한 NBT 를 띄고있음");
            return true;
        }

        // 두 아이템의 NBT 가 동일하지 않을경우
        else {

            Set<String> holdKeys = holdMainHandNBT.getKeys();
            Set<String> latestKeys = latestDateNBT.getKeys();
            Set<String> differentKeys = holdKeys.stream()
                    .filter(key -> {
                        ReadWriteNBT holdCompound = holdMainHandNBT.getCompound(key);
                        ReadWriteNBT latestCompound = latestDateNBT.getCompound(key);

                        // 둘 중 하나라도 null인 경우 비교
                        if (holdCompound == null || latestCompound == null) {
                            return holdCompound != latestCompound;
                        }

                        // 둘 다 null이 아닌 경우 값을 비교
                        return !holdCompound.toString().equals(latestCompound.toString());
                    })
                    .collect(Collectors.toSet());

            // 세밀한 정보 출력
            StringBuilder detailedDifference = new StringBuilder("손 아이템과 최신 아이템 간 다른 태그:\n");
            for (String key : differentKeys) {
                ReadWriteNBT holdValue = holdMainHandNBT.getCompound(key);
                ReadWriteNBT latestValue = latestDateNBT.getCompound(key);

                detailedDifference.append("태그: ").append(key).append("\n");
                detailedDifference.append(" - 손 아이템 값: ").append(holdValue != null ? holdValue.toString() : "null").append("\n");
                detailedDifference.append(" - 최신 아이템 값: ").append(latestValue != null ? latestValue.toString() : "null").append("\n");
            }

            SendMessage.DebugLog(player, detailedDifference.toString());

            return false;
        }

    }

    private static ItemStack ignoreAmount(ItemStack item) {
        if (item == null) return null;
        ItemStack copy = item.clone();
        copy.setAmount(1); // 수량을 1로 고정
        return copy;
    }



    // 특정 NBT 를 제외하고 비교하기위해, 특정 NBT 를 제거하는 메서드
    private static void removeComponentsNBT(Player player, ReadWriteNBT nbt) {
        SendMessage.DebugLog(player,"================================================================");
        ReadWriteNBT components = nbt.getCompound("components");

        // 컴포넌트가 null 이 아닐경우 (1.20.5+ 에서는 컴포넌트로 전체가 감싸져있음)
        if (components != null) {
            SendMessage.DebugLog(player,">> components (컴포넌트) 가 null 이 아님.");

            // 제외할 바닐라 NBT 태그 리스트를 콘피그에서 가져오기
            List<String> excludeVanillaNBTComponentTags = IgnoreNbtHandler.getInstance().getIgnoreVanillaNbtList();

            SendMessage.DebugLog(player,"콘피그에서 가져온 바닐라 태그 목록: " + excludeVanillaNBTComponentTags);


            // components 내부에서 제외할 바닐라 NBT 태그를 배열만큼 모두 삭제
            Set<String> componentKeys = components.getKeys();
            excludeVanillaNBTComponentTags.stream()
                    .filter(componentKeys::contains) // 키가 존재하는 경우만 필터링
                    .forEach(tag -> {
                        SendMessage.DebugLog(player," >> 제거된 바닐라 태그: " + tag);
                        components.removeKey(tag);
                    });

            // `minecraft:custom_data` 내부의 태그 처리
            ReadWriteNBT customData = components.getCompound("minecraft:custom_data");

            // custom_data 가 존재하는 경우 (MMOItems 의 커스텀 스탯을 표기하는 모든 NBT 가 custom_data 속에 존재하기 때문)
            if (customData != null) {

                SendMessage.DebugLog(player,"custom_data 태그가 비어있지 않음!");
                // SendMessage.DebugLog(player,"custom_data 전체 구조: " + customData);

                // 제외할 태그들을, MMOItems만이 가지는 커스텀 NBT 태그 리스트를 콘피그에서 가져오기
                List<String> excludeMmoitemsCustomNBTComponentTags = IgnoreNbtHandler.getInstance().getIgnoreMMOItemsStatNbtList();

                // "custom_data" 내부에 존재하는, MMOItems만이 가지는 커스텀 NBT 태그를 배열만큼 모두 삭제
                Set<String> customDataKeys = customData.getKeys();
                excludeMmoitemsCustomNBTComponentTags.stream()
                        .filter(customDataKeys::contains) // 키가 존재하는 경우만 필터링
                        .forEach(tag -> {
                            SendMessage.DebugLog(player," >> 제거된 MMOItems 커스텀 태그: " + tag);
                            customData.removeKey(tag);
                        });

                // `HSTRY_*` 키가 excludeMmoitemsCustomNBTComponentTags 에 포함된 경우
                if (excludeMmoitemsCustomNBTComponentTags.contains("HSTRY_*")) {
                    // HSTRY_ 로 시작되는 모든 NBT를 삭제
                    customDataKeys.stream()
                            .filter(key -> key.startsWith("HSTRY_"))
                            .forEach(key -> {
                                SendMessage.DebugLog(player," >> 제거된 HSTRY_ 태그: " + key);
                                customData.removeKey(key);
                            });
                }




                // custom_data={...} 내에서,
                // "MMOITEMS_" , "PublicBukkitValues", "HSTRY_" 문자가 포함되어 있지않은 NBT태그를 모두 제외할것인지 여부
                // MMOItems 의 "NBT Tags" 옵션으로, 아이템에 원하는 NBT 태그를 더 추가하면 (예시로 "예시_NBT_태그" 라는 NBT 태그)
                // MMOITEMS_CUSTOM_NBT 태그 안에 "예시_NBT_태그" 가 저장되지만, 불필요하게 추가로, custom_data={...} 내부 마지막줄에도 "예시_NBT_태그" NBT 태그가 붙게 됨.
                // 만약 젬스톤에 이러한 "NBT Tags" 옵션으로 추가한 NBT 태그가 있다면, 해당 젬스톤을 장착하고 제거한 후에도
                // 젬스톤을 한번 장착했던 아이템에 "예시_NBT_태그" 가 존재하여, 업데이트가 진행되는 상황이 발생함
                // 따라서, "MMOITEMS_" , "PublicBukkitValues", "HSTRY_" 문자가 포함되어 있지않은 NBT태그를 모두 제외하여, 이런 NBT 태그를 무시하도록
                // 설계
                if (excludeMmoitemsCustomNBTComponentTags.contains("not_contains_MMOITEMS_or_PublicBukkitValues_or_HSTRY")) {
                    // `customData`의 모든 키 가져오기
                    // Set<String> customDataKeys = customData.getKeys();

                    // 조건에 맞는 태그 제거
                    customDataKeys.stream()
                            // MMOITEMS_, PublicBukkitValues, HSTRY_ 3개 모두 포함되어 있지 않는 NBT를 제거 > removeKey()
                            .filter(key -> !key.startsWith("MMOITEMS_"))
                            .filter(key -> !key.equals("PublicBukkitValues"))
                            .filter(key -> !key.startsWith("HSTRY_"))
                            .forEach(key -> {
                                SendMessage.DebugLog(player," >> 제거된 기타 태그(MMOITEMS_, PublicBukkitValues, HSTRY_ 로 시작하지 않는 NBT태그): " + key);
                                customData.removeKey(key); // 태그 제거
                            });
                }

            }

        } else {
            SendMessage.DebugLog(player," >> components (컴포넌트) 가 null임");
        }

    }





}