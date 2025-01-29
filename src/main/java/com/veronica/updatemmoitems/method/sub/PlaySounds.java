package com.veronica.updatemmoitems.method.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaySounds {


    public static void playSounds(Player player, List<String> soundConfig) {

        // config 내용이 null 일 경우 종료
        if (soundConfig == null || soundConfig.isEmpty()) { return; }

        try {
            // 소리 설정 파싱
            String[] soundData = parseSoundConfig(soundConfig);

            // 소리 정보 추출
            String soundName = soundData[0];
            float volume = Float.parseFloat(soundData[1]);
            float pitch = Float.parseFloat(soundData[2]);

            // 플레이어에게 소리 재생
            player.playSound(player.getLocation(), soundName, volume, pitch);

        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            Bukkit.getLogger().warning("잘못된 사운드 설정");
        }
    }


    // 설정된 사운드 문자열 파싱
    private static String[] parseSoundConfig(List<String> soundConfig) {
        // 소리 설정 문자열 결합 및 대괄호 제거
        String configString = String.join(",", soundConfig)
                .replaceAll("[\\[\\]]", "")  // 대괄호 제거
                .trim();

        // 소리 설정 분리
        String[] parts = configString.split(",");

        // 소리 설정 정보가 부족한 경우 예외 발생
        if (parts.length < 3) {
            throw new IllegalArgumentException("사운드타입, volume, pitch 3개를 모두 작성하지 않았음.");
        }

        // 소리 설정 반환 (소리 이름, 볼륨, 피치)
        return new String[]{
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim()
        };
    }

}