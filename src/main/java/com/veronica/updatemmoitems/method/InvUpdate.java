package com.veronica.updatemmoitems.method;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.method.sub.*;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static com.veronica.updatemmoitems.method.sub.CheckLatest.isLatestMMOItems;
import static com.veronica.updatemmoitems.method.sub.GetItemsInfo.getMMOItemsInfo;

public class InvUpdate {
    private static final MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();

    public static void allInventoryUpdate(Player targetPlayer, Player commandSender, PlayerJoinEvent event){

        PlayerInventory inventory = targetPlayer.getInventory();
        int mmoItemsCount = 0;


        // 플레이어의 모든 인벤토리 슬롯 검사 (갑옷과 왼손 포함)
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack targetItem  = inventory.getItem(i);

            // 비어있는 경우, 현재 슬롯 건너뜀
            if (targetItem == null || targetItem.getType() == Material.AIR || !targetItem.hasItemMeta()) { continue; }

            MMOItem mmoItem = getMMOItemsInfo(targetItem);

            // MMOItems 아이템이 아닌 경우, 현재 슬롯 건너뜀
            if (mmoItem == null) { continue; }

            // whitelist 옵션이 꺼져있을 경우 또는 화이트리스트에서 허용된 Type이 감지될 경우 통과하지만 (true)
            // 화이트리스트에 작성된 태그와 일치하는 타입이 아닐경우(false), continue
            if (!Whitelist.whitelistCheck(targetItem)){ continue; }

            // updatedItem 변수에 해당 Type, ID 를 지닌 아이템 정보를 받아옴(즉, 해당 아이템의 가장 현재 정보를 받아옴)
            ItemStack updatedItem = mmoItem.newBuilder().build();

            // 업그레이드 가능한 아이템의 업데이트 기능이 비활성화 상태 + 업그레이드가 1강이라도 적용된 경우 >> 건너뜀
            if ( !(ConfigHandler.getInstance().getIsWorkUpgradableItems()) && (CheckUpgrade.isUpgradingItems(targetItem)) ){
                continue;
            }

            // 젬스톤 적용 시 업데이트가 비활성화 상태 + 젬스톤이 적용되어있는 아이템일 때 >> 건너뜀
            if ( !(ConfigHandler.getInstance().getIsWorkGemstoneApplied()) && (CheckGemStone.isInGemstone(targetItem)) ) {
                continue;
            }

            // 1. 내구도가 존재하는 아이템인지 확인
            // 2. 아이템의 내구도가 최대가 아닌지 확인 (한 번이라도 사용된 경우)
            // 3. 동시에 config 설정에서, "work-only-max-dura" 설정을 true 로 활성화 했을 경우, 현재 슬롯 건너뜀
            if (targetItem.getType().getMaxDurability() > 0 && targetItem.getDurability() > 0 && ConfigHandler.getInstance().getIsWorkMaxDurability()) {
                continue;
            }

            // 이미 최신화된 아이템일 경우, 현재 슬롯 건너뜀
            if (isLatestMMOItems(targetItem, updatedItem)) { continue; }

            // 인벤토리 슬롯에 있는 아이템의 수량을 가져옴
            int itemAmount = targetItem.getAmount();

            // 업데이트된 아이템을 생성
            ItemStack totalGiveItems = updatedItem.clone();

            // 해당 인벤토리 칸에 존재하는 아이템 수량만큼 수량을 설정
            totalGiveItems.setAmount(itemAmount);

            // 바닐라 인첸트데이터 및 AdvancedEnchantments 인첸트가 존재 시, 해당 데이터 유지
            totalGiveItems = EnchantData.setEnchantData(targetItem, totalGiveItems);

            // 현재 처리 중인 슬롯 번호
            int currentSlot = i;

            // 해당 인벤토리 칸에 해당 아이템으로 set
            inventory.setItem(currentSlot, totalGiveItems);

            mmoItemsCount += itemAmount;

        }

        // 업데이트된 아이템이 최소한 1개라도 있을때만 해당 메시지 출력
        if (mmoItemsCount > 0) {

            // 타인의 인벤토리를 업데이트시킨 사람인 commandSender 가 없는상태, 즉 자신의 인벤토리를 업데이트 시킨경우
            if(commandSender == null) {


                targetPlayer.sendMessage(miniMessage.deserialize(
                        Message.INVENTORY_UPDATE_SELF.getMessage(),
                        Placeholder.parsed("prefix", Message.PREFIX.getMessage()),
                        Placeholder.parsed("amount", String.valueOf(mmoItemsCount))
                ));

                // 성공 사운드 재생 (타깃 플레이어에게 == 자기 자신에게)
                PlaySounds.playSounds(targetPlayer,
                        ConfigHandler.getInstance().getSuccessSounds(),
                        ConfigHandler.getInstance().getSuccessVolume(),
                        ConfigHandler.getInstance().getSuccessPitch()
                );

            }

            // 타인의 인벤토리 업데이트를 사용하여 타인의 인벤토리를 업데이트 시킨경우
            else {
                // 명령어 수행자에게 메시지 보냄
                commandSender.sendMessage(miniMessage.deserialize(
                        Message.INVENTORY_UPDATE_OTHER.getMessage(),
                        Placeholder.parsed("prefix", Message.PREFIX.getMessage()),
                        Placeholder.parsed("player", targetPlayer.getName()),
                        Placeholder.parsed("amount", String.valueOf(mmoItemsCount))
                ));

                // 인벤토리 업데이트를 당한 타깃에게 "관리자에 의해 인벤토리의 아이템이 업데이트됨" 이라는 메시지를 보냄
                targetPlayer.sendMessage(miniMessage.deserialize(
                        Message.INVENTORY_UPDATE_NOTIFICATION.getMessage(),
                        Placeholder.parsed("prefix", Message.PREFIX.getMessage()),
                        Placeholder.parsed("amount", String.valueOf(mmoItemsCount))
                ));

                // 성공 사운드 재생 (업데이트 당한 타깃에게)
                PlaySounds.playSounds(targetPlayer,
                        ConfigHandler.getInstance().getSuccessSounds(),
                        ConfigHandler.getInstance().getSuccessVolume(),
                        ConfigHandler.getInstance().getSuccessPitch()
                );

                // 성공 사운드 재생 (업데이트 실행한 명령어 수행자에게)
                PlaySounds.playSounds(commandSender,
                        ConfigHandler.getInstance().getSuccessSounds(),
                        ConfigHandler.getInstance().getSuccessVolume(),
                        ConfigHandler.getInstance().getSuccessPitch()
                );
            }
        }

        // 업데이트 할 아이템이 없을 때 (이미 인벤토리의 아이템이 모두 최신 상태일 때)
        else {

            // 이벤트가 없음. 즉, 접속이벤트가 아니라, 명령어로 수행되었을 때에만 "이미 인벤토리의 모든 아이템이 최신 상태" 라는 메시지를 출력
            if(event == null ){

                // 타인의 인벤토리를 업데이트시킨 사람인 commandSender 가 없는상태, 즉 자신의 인벤토리를 업데이트 시킨경우
                if(commandSender == null) {

                    // "이미 내 인벤토리의 모든 아이템은 최신 상태입니다" 출력
                    targetPlayer.sendMessage(miniMessage.deserialize(
                            Message.INVENTORY_ALREADY_ALL_UPDATED.getMessage(),
                            Placeholder.parsed("prefix", Message.PREFIX.getMessage()),
                            Placeholder.parsed("amount", String.valueOf(mmoItemsCount))
                    ));

                    // 실패 사운드 재생
                    PlaySounds.playSounds(targetPlayer,
                            ConfigHandler.getInstance().getFailSounds(),
                            ConfigHandler.getInstance().getFailVolume(),
                            ConfigHandler.getInstance().getFailPitch()
                    );
                }

                // 타인의 인벤토리 업데이트를 사용하여 타인의 인벤토리를 업데이트 시킨경우
                else {
                    // 명령어 수행자에게 메시지 보냄
                    // "이미 해당 유저의 인벤토리의 모든 아이템은 최신 상태입니다" 출력
                    commandSender.sendMessage(miniMessage.deserialize(
                            Message.INVENTORY_ALREADY_ALL_UPDATED_OTHER.getMessage(),
                            Placeholder.parsed("prefix", Message.PREFIX.getMessage()),
                            Placeholder.parsed("player", targetPlayer.getName())
                    ));

                    // 실패 사운드 재생
                    PlaySounds.playSounds(commandSender,
                            ConfigHandler.getInstance().getFailSounds(),
                            ConfigHandler.getInstance().getFailVolume(),
                            ConfigHandler.getInstance().getFailPitch()
                    );

                }
            }

        }
    }

    // 이벤트 없이 호출할 수 있는 오버로드 메서드
    // 오버로드된 메서드 1: commandSender만 null인 경우
    public static void allInventoryUpdate(Player targetPlayer, PlayerJoinEvent event) {
        allInventoryUpdate(targetPlayer, null, event);
    }

    // event만 null인 경우
    public static void allInventoryUpdate(Player targetPlayer, Player commandSender) {
        allInventoryUpdate(targetPlayer, commandSender, null);
    }

    // commandSender와 event 모두 null인 경우
    public static void allInventoryUpdate(Player targetPlayer) {
        allInventoryUpdate(targetPlayer, null, null);
    }
}
