package com.veronica.updatemmoitems.listener;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.method.sub.CheckGemStone;
import com.veronica.updatemmoitems.method.sub.EnchantData;
import com.veronica.updatemmoitems.method.sub.PlaySounds;
import com.veronica.updatemmoitems.method.sub.Whitelist;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import static com.veronica.updatemmoitems.method.sub.CheckLatest.isLatestMMOItems;
import static com.veronica.updatemmoitems.method.sub.GetItemsInfo.getMMOItemsInfo;

public class JoinEvent implements Listener {

    private static final MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();

    @EventHandler
    public void plyerJoinEvent(PlayerJoinEvent event) {


        // config 에서 "work-join-update:" 옵션이 false 일 경우 종료
        if (!ConfigHandler.getInstance().getIsWorkJoinUpdate()){ return; }


        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        int mmoItemsCount = 0;


        // 플레이어의 모든 인벤토리 슬롯 검사 (갑옷과 왼손 포함)
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack targetItem  = inventory.getItem(i);

            // 비어있는 경우, 현재 슬롯 건너뜀
            if (targetItem == null || targetItem.getType() == Material.AIR || !targetItem.hasItemMeta()) { continue; }

            MMOItem mmoItem = getMMOItemsInfo(targetItem);

            // MMOItems 아이템이 아닌 경우, 현재 슬롯 건너뜀
            if (mmoItem == null) { continue; }

            // updatedItem 변수에 해당 Type, ID 를 지닌 아이템 정보를 받아옴(즉, 해당 아이템의 가장 현재 정보를 받아옴)
            ItemStack updatedItem = mmoItem.newBuilder().build();

            // 젬스톤 적용 시 업데이트가 비활성화 상태 + 젬스톤이 적용되어있는 아이템일 때, 현재 슬롯 건너뜀
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
            boolean isLatest = isLatestMMOItems(targetItem, updatedItem);
            if (isLatest) {
                continue;
            }

            // whitelist 옵션이 꺼져있을 경우 또는 화이트리스트에서 허용된 Type이 감지될 경우 통과하지만 (true)
            // 화이트리스트에 작성된 태그와 일치하는 타입이 아닐경우(false), continue
            if (!Whitelist.whitelistCheck(targetItem, player)){ continue; }

            // 인벤토리 슬롯에 있는 아이템의 수량을 가져옴
            int itemAmount = targetItem.getAmount();

            // 업데이트된 아이템을 생성
            ItemStack totalGiveItems = updatedItem.clone();

            // 해당 인벤토리 칸에 존재하는 아이템 수량만큼 수량을 설정
            totalGiveItems.setAmount(itemAmount);

            // 바닐라 인첸트데이터 존재 시, 해당 데이터 유지
            totalGiveItems = EnchantData.setEnchantData(targetItem, totalGiveItems);

            // 현재 처리 중인 슬롯 번호
            int currentSlot = i;

            // 해당 인벤토리 칸에 해당 아이템으로 set
            inventory.setItem(currentSlot, totalGiveItems);

            mmoItemsCount += itemAmount;

        }

        // 업데이트된 아이템이 최소한 1개라도 있을때만 해당 메시지 출력
        if (mmoItemsCount > 0) {
            player.sendMessage(miniMessage.deserialize(
                    Message.UPDATE_JOIN_EVENT.getMessage(),
                    Placeholder.parsed("prefix", Message.PREFIX.getMessage()),
                    Placeholder.parsed("amount", String.valueOf(mmoItemsCount))
            ));
        }

    }
}
