package com.veronica.updatemmoitems.config;

import com.veronica.updatemmoitems.UpdateMMOItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class ConfigHandler {
    private static ConfigHandler instance;
    private final FileConfiguration config;

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


    // 업데이트 된 콘피그의 정보를 변수에 저장하는 메서드
    public void reloadConfigOptions() {

        Plugin instance = UpdateMMOItems.getInstance();

        Message.reloadConfigMessage(); // Message 클래스에 따로 분리해놓은 것들

        FileConfiguration config = UpdateMMOItems.getInstance().getConfig();

        successSounds = config.getString("sounds.success.sound", "ENTITY_PLAYER_LEVELUP");
        successVolume = (float)config.getDouble("sounds.success.volume", 1.0);
        successPitch = (float)config.getDouble("sounds.success.pitch", 1.0);

        failSounds = config.getString("sounds.fail.sound", "BLOCK_NOTE_BLOCK_COW_BELL");
        failVolume = (float)config.getDouble("sounds.fail.volume", 0.9);
        failPitch = (float)config.getDouble("sounds.fail.pitch", 1.0);
    }


    public String getSuccessSounds() {
        return successSounds;
    }
    public float getSuccessVolume() { return successVolume; }
    public float getSuccessPitch() { return successPitch; }



    public String getFailSounds() { return failSounds; }
    public float getFailVolume() { return failVolume; }
    public float getFailPitch() { return failPitch; }

}
