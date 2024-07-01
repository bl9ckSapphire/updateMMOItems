package com.veronica.updatemmoitems.method;

import com.veronica.updatemmoitems.UpdateMMOItems;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.Message;
import com.veronica.updatemmoitems.method.sub.PlaySounds;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static com.veronica.updatemmoitems.method.sub.CheckLatest.isLatestMMOItems;
import static com.veronica.updatemmoitems.method.sub.GetItemsInfo.getMMOItemsInfo;

public class Update {

    private static final MiniMessage miniMessage = UpdateMMOItems.getMiniMessage();

    // 아이템을 업데이트하는 메서드
    public static void updateItem(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        PlayerInventory inventory = player.getInventory();

        // 메인핸드에 아이템을 들고 있는지 확인
        if (itemInHand == null || itemInHand.getType().isAir() || itemInHand.getAmount() == 0) {
            player.sendMessage(miniMessage.deserialize(Message.IS_AIR.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

            PlaySounds.playSounds(player,
                    ConfigHandler.getInstance().getFailSounds(),
                    ConfigHandler.getInstance().getFailVolume(),
                    ConfigHandler.getInstance().getFailPitch()
            );
            return;
        }

        MMOItem mmoItem = getMMOItemsInfo(itemInHand);

        // MMOItems 아이템이 아닌 경우
        if (mmoItem == null) {
            player.sendMessage(miniMessage.deserialize(Message.NO_MMOITEMS.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

            PlaySounds.playSounds(player,
                    ConfigHandler.getInstance().getFailSounds(),
                    ConfigHandler.getInstance().getFailVolume(),
                    ConfigHandler.getInstance().getFailPitch()
            );
            return;
        }

        // updatedItem 변수에 해당 Type, ID 를 지닌 아이템 정보를 받아옴(즉, 해당 아이템의 가장 현재 정보를 받아옴)
        ItemStack updatedItem = mmoItem.newBuilder().build();

        // updatedItem 이 null 일때, 즉 해당 아이템이 더 이상 존재하지 않는 MMOItems 일 때, 종료
        if (updatedItem == null) {
            player.sendMessage(miniMessage.deserialize(Message.NO_LONGER_EXISTS.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

            PlaySounds.playSounds(player,
                    ConfigHandler.getInstance().getFailSounds(),
                    ConfigHandler.getInstance().getFailVolume(),
                    ConfigHandler.getInstance().getFailPitch()
            );

            return;
        }

        // 아이템의 내구도가 최대가 아닌지 확인 (한 번이라도 사용된 경우)
        // 동시에 config 설정에서, "work-only-max-dura" 설정을 true 로 활성화 했을 경우
        if (itemInHand.getDurability() > 0 && ConfigHandler.getInstance().getIsWorkMaxDurability()) {
            player.sendMessage(miniMessage.deserialize(Message.USED_ITEM.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

            PlaySounds.playSounds(player,
                    ConfigHandler.getInstance().getFailSounds(),
                    ConfigHandler.getInstance().getFailVolume(),
                    ConfigHandler.getInstance().getFailPitch()
            );
            return;
        }


        // 이미 최신화된 아이템일 경우, 종료
        boolean isLatest = isLatestMMOItems(itemInHand, updatedItem);
        if (isLatest) {
            player.sendMessage(miniMessage.deserialize(Message.ALREADY_LATEST.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

            PlaySounds.playSounds(player,
                    ConfigHandler.getInstance().getFailSounds(),
                    ConfigHandler.getInstance().getFailVolume(),
                    ConfigHandler.getInstance().getFailPitch()
            );
            return;
        }

        // 인벤토리에 빈 공간이 있는지 확인
        if (inventory.firstEmpty() == -1) {
            player.sendMessage(miniMessage.deserialize(Message.NO_INVENTORY_SPACE.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

            PlaySounds.playSounds(player,
                    ConfigHandler.getInstance().getFailSounds(),
                    ConfigHandler.getInstance().getFailVolume(),
                    ConfigHandler.getInstance().getFailPitch()
            );
            return;
        }

        // 메인핸드에 있는 아이템의 수량을 1 감소시킴
        itemInHand.setAmount(itemInHand.getAmount() - 1);

        // 업데이트된 아이템을 인벤토리에 추가
        inventory.addItem(updatedItem);
        player.sendMessage(miniMessage.deserialize(Message.SUCCESS_UPDATE.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));

        PlaySounds.playSounds(player,
                ConfigHandler.getInstance().getSuccessSounds(),
                ConfigHandler.getInstance().getSuccessVolume(),
                ConfigHandler.getInstance().getSuccessPitch()
        );
    }


}
