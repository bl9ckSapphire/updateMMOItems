package com.veronica.updatemmoitems.listener;

import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.method.Update;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEvent implements Listener {
    // 젬스톤 슬롯 장착 플러그인 쪽의 인벤토리 클릭 우선순위는 HIGH
    /*
    이벤트의 우선순위는 다음과 같은 순서로 실행
    LOWEST → LOW → NORMAL → HIGH → HIGHEST → MONITOR
    하지만 이것을 반대로 생각하고 코딩해야함

    예).
    우선순위 HIGH 인 곳에서 이벤트가 캔슬되었고
    우선순위 HIGHEST 인 곳에서
     if (event.isCancelled()) { return; }
    이 코드가 존재한다면,
    우선순위 HIGHEST 인 곳이 이벤트가 캔슬되었다는것을 감지하고 return; 하게 됨

    즉, HIGH 인 곳에서 이벤트가 캔슬되었다면,
    HIGHEST 에서 이 사실을 알고, HIGHEST 코드에서  if (event.isCancelled()) { return; } 을 사용하여 종료할 수 있음
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        // 이미 취소된 이벤트인 경우 종료
        if (event.isCancelled()) { return; }

        // 모루 인벤토리에서 클릭한 경우 종료
        if (event.getInventory().getType() == InventoryType.ANVIL) { return; }

        // config 에서 "work-cursor-click:" 옵션이 false 일 경우 종료
        if (!ConfigHandler.getInstance().getIsWorkCursorClick()){ return; }

        // 클릭한 플레이어 받아오기
        Player player = (Player) event.getWhoClicked();

        // 플레이어의 게임 모드를 확인
        GameMode gameMode = player.getGameMode();

        // 플레이어가 겜모상태인 동시에 "work-gamemode-creative:" 옵션이 false 로, 비활성화 되있을 경우 종료
        if (gameMode == GameMode.CREATIVE && !(ConfigHandler.getInstance().getIsWorkCreative()) ) { return; }

        // 클릭한 슬롯 받아오기
        ItemStack currentItem = event.getCurrentItem();

        // 비어있는 곳 클릭시 종료
        if (currentItem == null || currentItem.getType() == Material.AIR ) { return; }

        // 커서에 있는 아이템을 가져옴
        ItemStack cursorItem = event.getCursor();

        // 커서에 이미 아이템을 집어 둔 상태에서 클릭한 경우 종료
        if (cursorItem != null && cursorItem.getType() != Material.AIR) { return; }

        // 업데이트 로직 수행
        Update.updateItem(player, event);


    }
}
