package com.veronica.updatemmoitems.method;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.method.sub.*;
import com.veronica.updatemmoitems.method.sub.check.CheckGemStone;
import com.veronica.updatemmoitems.method.sub.check.CheckOtherPluginItem;
import com.veronica.updatemmoitems.method.sub.set.SetDurability;
import com.veronica.updatemmoitems.method.sub.set.SetEnchant;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static com.veronica.updatemmoitems.method.sub.check.CheckLatest.isLatestCustomItems;
import static com.veronica.updatemmoitems.method.sub.get.GetLatestItems.getLatestCustomItems;

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

            // Nexo, Oraxen, ItemsAdder 아이템인 경우 종료 (MI 데이터를 참조할 수 있는 것들은 사전에 배제시킴)
            if (CheckOtherPluginItem.isOtherPluginItems(null, targetItem)) { return; }


            // 업그레이드 정보까지 타킷아이템과 동일하게 씌운 latestMMOItems 를
            // ItemStack 자료형인 latestCustomItem 으로 선언
            // (업그레이드 정보까지 똑같이 씌운 최신 정보의 아이템)
            ItemStack latestCustomItem = getLatestCustomItems(targetItem);


            // 해당 아이템의 최신 아이템 정보가 없는경우 (없는 아이템인 경우), 현재 슬롯 건너뜀
            if (latestCustomItem == null) { continue; }

            // whitelist 옵션이 꺼져있을 경우 또는 화이트리스트에서 허용된 Type이 감지될 경우 통과하지만 (true)
            // 화이트리스트에 작성된 태그와 일치하는 타입이 아닐경우(false), continue
            if (!Whitelist.whitelistCheck(targetItem)){ continue; }


            // 이미 최신화된 아이템일 경우, 현재 슬롯 건너뜀
            if (isLatestCustomItems(null, targetItem, latestCustomItem)) { continue; }


            // 인벤토리 슬롯에 있는 아이템의 수량을 가져옴
            int itemAmount = targetItem.getAmount();

            // 최종 지급할 totalGiveItems 변수에 latestCustomItem(해당 아이템의 최신 정보) 대입
            ItemStack totalGiveItems = latestCustomItem;

            // 해당 인벤토리 칸에 존재하는 아이템 수량만큼 수량을 설정
            totalGiveItems.setAmount(itemAmount);

            // 바닐라 인첸트데이터 및 AdvancedEnchantments 인첸트가 존재 시, 해당 데이터 유지
            totalGiveItems = SetEnchant.setEnchantData(targetItem, totalGiveItems);

            // 콘피그의 maintaining-durability 가 true 로, 내구도를 유지시키면서 업데이트 한다면
            if(ConfigHandler.getInstance().getIsEnableMaintainingDurability()){
                // 들고있던 아이템의 손상된 내구도만큼의 내구도를 깎아 지급
                // 명령어를 사용한 commandSender 가 op 인지 확인해야 하니 매개변수에 commandSender
                totalGiveItems = SetDurability.applyDamageToUpdatedItem(commandSender, targetItem, totalGiveItems);
            }


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
                PlaySounds.playSounds(targetPlayer, ConfigHandler.getInstance().getSuccessSounds());

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
                PlaySounds.playSounds(targetPlayer, ConfigHandler.getInstance().getSuccessSounds());

                // 성공 사운드 재생 (업데이트 실행한 명령어 수행자에게)
                PlaySounds.playSounds(commandSender, ConfigHandler.getInstance().getSuccessSounds());
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
                    PlaySounds.playSounds(targetPlayer, ConfigHandler.getInstance().getFailSounds());
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
                    PlaySounds.playSounds(commandSender, ConfigHandler.getInstance().getFailSounds());

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
