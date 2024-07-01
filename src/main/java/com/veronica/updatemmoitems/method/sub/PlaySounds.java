package com.veronica.updatemmoitems.method.sub;

import com.veronica.updatemmoitems.UpdateMMOItems;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class PlaySounds {
    private static final Logger logger = UpdateMMOItems.getPluginLogger();

    public static void playSounds(Player player, String soundKey, float volume, float pitch) {
        if (soundKey != null && !soundKey.isEmpty()) {
            try {
                Sound sound = Sound.valueOf(soundKey);
                player.playSound(player.getLocation(), sound, volume, pitch);
            } catch (IllegalArgumentException e) {
                // 로그에 오류를 기록하거나 기본 사운드를 재생할 수 있습니다.
                logger.severe(soundKey + " 는 올바르지 않은 사운드 형식입니다..");
            }
        }
    }
}