package com.veronica.updatemmoitems.listener;

import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.method.InvUpdate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinEvent implements Listener {



    @EventHandler
    public void plyerJoinEvent(PlayerJoinEvent event) {

        // config 에서 "work-join-update:" 옵션이 false 일 경우 종료
        if (!ConfigHandler.getInstance().getIsWorkJoinUpdate()){ return; }

        Player player = event.getPlayer();

        // 인벤토리의 모든 아이템을 검사, 업데이트 시키는 메서드
        InvUpdate.allInventoryUpdate(player,null, event);

    }
}
