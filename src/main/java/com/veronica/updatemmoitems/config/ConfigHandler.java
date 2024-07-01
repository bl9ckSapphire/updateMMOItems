package com.veronica.updatemmoitems.config;

import com.veronica.updatemmoitems.UpdateMMOItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.File;

import java.util.logging.Logger;

public class ConfigHandler {
    private static ConfigHandler instance;
    private final FileConfiguration config;
    private boolean isMaxDurability;

    private String successSounds;
    private float successVolume;
    private float successPitch;

    private String failSounds;
    private float failVolume;
    private float failPitch;

    private ConfigHandler() {
        config = UpdateMMOItems.getInstance().getConfig();
    }

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    // config.yml 존재 여부 확인
    private boolean configFileExists(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        return configFile.exists();
    }

    // 업데이트 된 콘피그의 정보를 변수에 저장하는 메서드
    public void reloadConfigOptions() {
        Plugin plugin = UpdateMMOItems.getInstance();

        // 콘피그 파일이 존재하지 않으면 새로 생성
        if (!configFileExists(plugin)) {
            plugin.saveDefaultConfig();
            plugin.getLogger().info("config.yml 이 존재하지 않아, 새로 생성됨.");
        }

        Message.reloadConfigMessage(); // Message 클래스에 따로 분리해놓은 것들
        FileConfiguration config = UpdateMMOItems.getInstance().getConfig();

        // 최대 내구도일 때만 작동시킬건지 결정하는 콘피그 데이터 (기본값 true)
        isMaxDurability = config.getBoolean("options.work-only-max-dura", true);

        // 업데이트 성공 사운드 관련 콘피그 데이터
        successSounds = config.getString("sounds.success.sound", "ENTITY_PLAYER_LEVELUP");
        successVolume = (float)config.getDouble("sounds.success.volume", 1.0);
        successPitch = (float)config.getDouble("sounds.success.pitch", 1.0);

        // 업데이트 실패 사운드 관련 콘피그 데이터
        failSounds = config.getString("sounds.fail.sound", "BLOCK_NOTE_BLOCK_COW_BELL");
        failVolume = (float)config.getDouble("sounds.fail.volume", 0.9);
        failPitch = (float)config.getDouble("sounds.fail.pitch", 1.0);
    }



    public boolean getIsWorkMaxDurability() { return isMaxDurability; }

    public String getSuccessSounds() {
        return successSounds;
    }
    public float getSuccessVolume() { return successVolume; }
    public float getSuccessPitch() { return successPitch; }

    public String getFailSounds() { return failSounds; }
    public float getFailVolume() { return failVolume; }
    public float getFailPitch() { return failPitch; }

}
