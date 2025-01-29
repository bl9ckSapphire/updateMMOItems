package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendMessage {
    public static void DebugLog(Player player, String msg){

        // 플레이어가 null 또는 not op 또는 콘피그에서 디버깅 로그 옵션이 false 일 경우 종료
        // invUpdate() 에서는 player 를 null 로 설정하여, 로그메시지가 뜨지 않도록 해야 함. (인벤토리 전체 업데이트 시, 디버깅 로그가 떠버리면 너무 도배가 됨)
        if( player == null ||!player.isOp() || !ConfigHandler.getInstance().getIsDebugEnable()) { return; }

        Bukkit.getLogger().info("[updateMMOItems-디버그]"+msg);
    }
}
