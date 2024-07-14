package com.veronica.updatemmoitems.config;

import com.veronica.updatemmoitems.UpdateMMOItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.File;

import java.util.List;
import java.util.logging.Logger;

public class ConfigHandler {
    private static ConfigHandler instance;
    private final FileConfiguration config;
    private boolean isMaxDurability;
    private boolean isWorkCursorClick;
    private boolean isMaintainingVanillaEnchantment;
    private boolean isWorkCreative;
    private boolean isWorkGemstoneApplied;
    private boolean isWorkJoinUpdate;

    private boolean isWorkWhitelist;

    private List<String> whitelistTypeList;


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

        // 인벤토리 창에서 아이템 클릭 시, 업데이트 수행하는 옵션 (기본값 false)
        isWorkCursorClick = config.getBoolean("options.cursor-click.enable", false);

        // 업데이트 진행 시, 아이템에 발려진 바닐라 인첸트 데이터를 유지할건지 판단하는 옵션(기본값 true)
        isMaintainingVanillaEnchantment = config.getBoolean("options.maintaining-vanilla-enchantment-data", true);

        // 겜모상태에서 커서클릭 업데이트 기능 작동할지 여부를 판단하는 옵션 (기본값 false)
        isWorkCreative = config.getBoolean("options.cursor-click.work-gamemode-creative", false);

        // 젬스톤이 박혀있는 상태에서도 업데이트 작동할지 판단하는 옵션 (기본값 false)
        isWorkGemstoneApplied = config.getBoolean("options.work-with-gem-stone-applied", false);

        // 서버 접속 시, 인벤토리에 있는 아이템들을 자동으로 업데이트 할 건지 판단하는 옵션 (기본값 false)
        isWorkJoinUpdate = config.getBoolean("options.join-update", false);

        // 업데이트 가능한 아이템 type 화이트리스트 옵션의 활성화 비활성화 여부 판단 (기본값 false)
        isWorkWhitelist = config.getBoolean("whitelist.enable", false);

        // 화이트리스트에 작성된 아이템 type 들의 값을 가져옴
        whitelistTypeList = config.getStringList("whitelist.type-list");


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
    public boolean getIsWorkCursorClick() { return isWorkCursorClick; }
    public boolean getIsMaintainingVanillaEnchantment() { return isMaintainingVanillaEnchantment; }
    public boolean getIsWorkGemstoneApplied() { return isWorkGemstoneApplied; }
    public boolean getIsWorkCreative() { return isWorkCreative; }
    public boolean getIsWorkJoinUpdate() { return isWorkJoinUpdate; }
    public boolean getIsWorkWhitelist() { return isWorkWhitelist; }
    public List<String> getWhitelistTypeList() { return whitelistTypeList; }


    public String getSuccessSounds() {
        return successSounds;
    }
    public float getSuccessVolume() { return successVolume; }
    public float getSuccessPitch() { return successPitch; }

    public String getFailSounds() { return failSounds; }
    public float getFailVolume() { return failVolume; }
    public float getFailPitch() { return failPitch; }

}
