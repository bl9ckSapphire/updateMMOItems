package com.veronica.updatemmoitems.method.sub.set;

import com.veronica.updatemmoitems.method.sub.SendMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SetDurability {

    // targetItem > 손에 들고있는 업데이트를 수행할 아이템
    // updatedItem > 손에 들고있는 MMOItems 의 최신 데이터

    // 업데이트 후의 아이템이, 내구도의 총 량 옵션이 변해서, 업뎃 전 100 에서 업뎃 후 200으로 증가한 변화가 생기더라도
    // 업데이트 시 깎인 내구도 유지옵션 활성화 + 깎인 내구도가 25 였다면 (따라서 업데이트 전 아이템의 남은 내구도가 75였다면)
    //   >> 175 의 남은 내구도를 가진 업데이트 된 아이템으로 정상적으로 적용되는걸 테스트했음
    public static ItemStack applyDamageToUpdatedItem(Player player, ItemStack targetItem, ItemStack updatedItem) {

        if (targetItem == null || updatedItem == null) {
            throw new IllegalArgumentException("ItemStack 이 null 상태임..");
        }

        // targetItem의 내구도 정보를 가져오기
        ItemMeta targetMeta = targetItem.getItemMeta();
        int damageValue = 0; // 기본값

        // targetItem이 방어구, 무기, 도구인지 확인
        if (isDamageableItem(targetItem)) {
            if (targetMeta instanceof Damageable) {
                damageValue = ((Damageable) targetMeta).getDamage();
                SendMessage.DebugLog(player, ">> 업데이트 수행할 아이템의 현재 내구도 손상값: " + damageValue);
            } else {
                SendMessage.DebugLog(player, ">> 해당 아이템은 내구도가 깎일 수 없는 아이템임.");
            }
        } else {
            SendMessage.DebugLog(player, ">> 해당 아이템은 방어구, 무기, 도구가 아니므로 내구도 손상을 적용하지 않음.");
        }

        // updatedItem에 내구도 손상 적용
        ItemMeta updatedMeta = updatedItem.getItemMeta();

        // updatedItem이 방어구, 무기, 도구인지 확인
        if (isDamageableItem(updatedItem)) {
            if (updatedMeta instanceof Damageable) {
                Damageable updatedDamageable = (Damageable) updatedMeta;

                int currentDamage = updatedDamageable.getDamage(); // 현재 내구도 손상값
                int newDamage = Math.min(currentDamage + damageValue, updatedItem.getType().getMaxDurability());
                updatedDamageable.setDamage(newDamage);

                SendMessage.DebugLog(player, ">> 지급할 새 아이템에 적용된 내구도 손상값: " + newDamage);
                updatedItem.setItemMeta((ItemMeta) updatedDamageable);
            } else {
                SendMessage.DebugLog(player, ">> 지급할 새 아이템(최신 MI)은, 내구도 손상을 적용할 수 없는 아이템임.");
            }
        } else {
            SendMessage.DebugLog(player, ">> 해당 아이템은 방어구, 무기, 도구가 아니므로 내구도 손상을 적용하지 않음.");
        }

        return updatedItem;
    }

    // 아이템이 방어구, 무기, 도구인지 확인하는 메서드
    private static boolean isDamageableItem(ItemStack item) {
        Material material = item.getType();

        // 방어구, 무기, 도구인지 확인
        return material.name().endsWith("_HELMET") || // 헬멧
                material.name().endsWith("_CHESTPLATE") || // 흉갑
                material.name().endsWith("_LEGGINGS") || // 레깅스
                material.name().endsWith("_BOOTS") || // 부츠
                material.name().endsWith("_SWORD") || // 검
                material.name().endsWith("_AXE") || // 도끼
                material.name().endsWith("_PICKAXE") || // 곡괭이
                material.name().endsWith("_SHOVEL") || // 삽
                material.name().endsWith("_HOE") || // 괭이
                material == Material.BOW || // 활
                material == Material.CROSSBOW || // 쇠뇌
                material == Material.ELYTRA || // 겉날개
                material.name().endsWith("_TRIDENT"); // 삼지창

    }
}