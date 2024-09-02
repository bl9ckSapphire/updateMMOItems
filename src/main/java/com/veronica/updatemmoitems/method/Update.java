package com.veronica.updatemmoitems.method;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.method.sub.*;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


import static com.veronica.updatemmoitems.method.sub.CheckLatest.isLatestCustomItems;
import static com.veronica.updatemmoitems.method.sub.GetLatestItems.getLatestCustomItems;

public class Update {

    private static final MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();

    // 아이템을 업데이트하는 메서드
    public static void updateItem(Player player, InventoryClickEvent event) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        PlayerInventory inventory = player.getInventory();

        // 이벤트 타입에 따라 업데이트할 아이템을 결정
        ItemStack targetItem = null;
        Inventory targetInventory = null;

        // InventoryClickEvent 자료형 매개변수를 받지 않았을 때
        if (event == null) {
            targetItem = itemInHand;
            targetInventory = inventory;
        }

        // InventoryClickEvent 자료형 매개변수를 받았을 때
        else {
            targetItem = event.getCurrentItem();
            targetInventory = event.getClickedInventory();
        }



        // 해당 아이템이 비어있는 상태, 없는상태
        // (혹시나 모를 결함을 위해, Update 메서드 안에서 한번 더 검사)
        if (targetItem == null || targetInventory == null || targetItem.getType() == Material.AIR) {

            // 클릭이벤트가 아닌(null)경우 "메시지+사운드" 를 재생 & 출력
            // 만약 이런 이벤트 수행중에도 메시지가 전송된다면 채팅창이 메시지로 도배될 것임.
            // 따라서 이벤트로 수행되는것이 아닌, "/업데이트" 명령어 사용 시에만 실패 메시지가 전송되도록
            if (event == null) {
                player.sendMessage(miniMessage.deserialize(Message.NO_INCORRECT_ITEMS.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                PlaySounds.playSounds(player,
                        ConfigHandler.getInstance().getFailSounds(),
                        ConfigHandler.getInstance().getFailVolume(),
                        ConfigHandler.getInstance().getFailPitch()
                );
            }
            return;
        }

        // MMOItem mmoItem = getMMOItemsInfo(targetItem);

        // 해당 아이템이 IA, Oraxen, MMOItems 인 경우, 해당 아이템을 최신 상태로 받아옴
        ItemStack latestCustomItem = getLatestCustomItems(targetItem);



        // 해당 아이템의 최신 아이템 정보가 없는경우 (없는 아이템인 경우)
        if (latestCustomItem == null) {
            // PlayerInteractEvent, InventoryClickEvent 가 아닌경우 "메시지+사운드" 를 재생 & 출력
            // 만약 이런 이벤트 수행중에도 메시지가 전송된다면 채팅창이 메시지로 도배될 것임.
            // 따라서 이벤트로 수행되는것이 아닌, "/업데이트" 명령어 사용 시에만 실패 메시지가 전송되도록
            if (event == null) {
                player.sendMessage(miniMessage.deserialize(Message.NO_INCORRECT_ITEMS.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                PlaySounds.playSounds(player,
                        ConfigHandler.getInstance().getFailSounds(),
                        ConfigHandler.getInstance().getFailVolume(),
                        ConfigHandler.getInstance().getFailPitch()
                );
            }
            return;
        }

        // whitelist 옵션이 꺼져있을 경우 또는 화이트리스트에서 허용된 Type이 감지될 경우 통과하지만 (true)
        // 화이트리스트에 작성된 태그와 일치하는 타입이 아닐경우(false), 해당 if 블록안의 코드가 수행됨
        if (!Whitelist.whitelistCheck(targetItem)){
            if (event == null) {
                player.sendMessage(miniMessage.deserialize(Message.NO_WHITELIST_ITEMS.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                PlaySounds.playSounds(player,
                        ConfigHandler.getInstance().getFailSounds(),
                        ConfigHandler.getInstance().getFailVolume(),
                        ConfigHandler.getInstance().getFailPitch()
                );
            }
            return;
        }

        // 업그레이드 가능한 아이템의 업데이트 기능이 비활성화 상태 + 업그레이드가 1강이라도 적용된 경우 > 종료
        if ( !(ConfigHandler.getInstance().getIsWorkUpgradableItems()) && (CheckUpgrade.isUpgradingItems(targetItem)) ) {
            if (event == null) {
                player.sendMessage(miniMessage.deserialize(Message.UPGRADABLE_ITEMS.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                PlaySounds.playSounds(player,
                        ConfigHandler.getInstance().getFailSounds(),
                        ConfigHandler.getInstance().getFailVolume(),
                        ConfigHandler.getInstance().getFailPitch()
                );
            }
            return;
        }

        // 젬스톤 적용 시 업데이트가 비활성화 상태 + 젬스톤이 적용되어있는 아이템일 때 > 종료
        if ( !(ConfigHandler.getInstance().getIsWorkGemstoneApplied()) && (CheckGemStone.isInGemstone(targetItem)) ) {
            if (event == null) {
                player.sendMessage(miniMessage.deserialize(Message.DETECTED_GEMSTONE.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                PlaySounds.playSounds(player,
                        ConfigHandler.getInstance().getFailSounds(),
                        ConfigHandler.getInstance().getFailVolume(),
                        ConfigHandler.getInstance().getFailPitch()
                );
            }
            return;
        }


        // updatedItem 변수에 해당 Type, ID 를 지닌 아이템 정보를 받아옴(즉, 해당 아이템의 가장 현재 정보를 받아옴)
        //ItemStack updatedItem = mmoItem.newBuilder().build();


        // 1. 내구도가 존재하는 아이템인지 확인
        // 2. 아이템의 내구도가 최대가 아닌지 확인 (한 번이라도 사용된 경우)
        // 3. 동시에 config 설정에서, "work-only-max-dura" 설정을 true 로 활성화 했을 경우
        if (targetItem.getType().getMaxDurability() > 0 && targetItem.getDurability() > 0 && ConfigHandler.getInstance().getIsWorkMaxDurability()) {
            if (event == null) {
                player.sendMessage(miniMessage.deserialize(Message.USED_ITEM.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                PlaySounds.playSounds(player,
                        ConfigHandler.getInstance().getFailSounds(),
                        ConfigHandler.getInstance().getFailVolume(),
                        ConfigHandler.getInstance().getFailPitch()
                );
            }

            return;
        }

        // 이미 최신화된 아이템일 경우, 종료
        if (isLatestCustomItems(targetItem, latestCustomItem)) {
            if (event == null) {
                player.sendMessage(miniMessage.deserialize(Message.ALREADY_LATEST.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

                PlaySounds.playSounds(player,
                        ConfigHandler.getInstance().getFailSounds(),
                        ConfigHandler.getInstance().getFailVolume(),
                        ConfigHandler.getInstance().getFailPitch()
                );
            }

            return;
        }


        // 인벤토리에 빈 공간이 있는지 확인 (InventoryClickEvent일 때는 확인하지 않음)
        if (!(event instanceof InventoryClickEvent) && inventory.firstEmpty() == -1) {
            if (event == null) {
                player.sendMessage(miniMessage.deserialize(Message.NO_INVENTORY_SPACE.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
                PlaySounds.playSounds(player,
                        ConfigHandler.getInstance().getFailSounds(),
                        ConfigHandler.getInstance().getFailVolume(),
                        ConfigHandler.getInstance().getFailPitch()
                );
            }

            return;
        }


        // InventoryClickEvent 이벤트가 아닐 때(null) 아이템 제거 및 지급 기능 + 전송되는 메시지
        if (event == null) {
            // 메인핸드에 있는 아이템의 수량을 가져옴
            int itemAmount = itemInHand.getAmount();

            // 최종 지급할 totalGiveItems 변수에 latestCustomItem(해당 아이템의 최신 정보) 대입
            ItemStack totalGiveItems = latestCustomItem;

            // 메인핸드에 들고있는 아이템 수량만큼 수량을 설정
            totalGiveItems.setAmount(itemAmount);

            // 바닐라 인첸트데이터 및 AdvancedEnchantments 인첸트가 존재 시, 해당 데이터 유지
            totalGiveItems = EnchantData.setEnchantData(targetItem, totalGiveItems);

            // 메인핸드의 아이템을 제거
            player.getInventory().setItemInMainHand(null);

            // 업데이트된 아이템을 인벤토리에 추가
            inventory.addItem(totalGiveItems);

            // 성공 메시지 전송 (명령어 사용으로 인한 업데이트 성공 메시지 전송)
            player.sendMessage(miniMessage.deserialize(Message.SUCCESS_UPDATE.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

        }

        // InventoryClickEvent 이벤트일 때 아이템 제거 및 지급 기능 + 전송되는 메시지
        // 인벤토리 슬롯을 클릭했을 때 아이템 업데이트 지급 관련 기능 로직
        else {
            // 클릭된 슬롯의 아이템을 가져옴
            ItemStack clickedItem = targetItem;

            // 클릭된 슬롯이 비어있는 경우 처리
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }

            // 클릭된 아이템의 수량을 가져옴
            int itemAmount = clickedItem.getAmount();

            // 최종 지급할 totalGiveItems 변수에 latestCustomItem(해당 아이템의 최신 정보) 대입
            ItemStack totalGiveItems = latestCustomItem;

            // 메인핸드에 들고있는 아이템 수량만큼 수량을 설정
            totalGiveItems.setAmount(itemAmount);

            // 바닐라 인첸트데이터 존재 시, 해당 데이터 유지
            totalGiveItems = EnchantData.setEnchantData(targetItem, totalGiveItems);

            // 클릭된 슬롯에 업데이트된 아이템을 추가
            event.getClickedInventory().setItem(event.getSlot(), totalGiveItems);

            // 손에 들고 있는 아이템을 제거
            if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                event.setCursor(null);
            }

            // 성공 메시지 전송 (커서 클릭 시 업데이트 성공 메시지 전송)
            player.sendMessage(miniMessage.deserialize(Message.CURSOR_CLICK_UPDATE.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

            // 이벤트 취소
            event.setCancelled(true);
        }


        // 성공 사운드 재생
        PlaySounds.playSounds(player,
            ConfigHandler.getInstance().getSuccessSounds(),
                ConfigHandler.getInstance().getSuccessVolume(),
                ConfigHandler.getInstance().getSuccessPitch()
        );
    }

    // 이벤트 없이 호출할 수 있는 오버로드 메서드
    public static void updateItem(Player player) {
        updateItem(player, null);
    }
}
