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

    private boolean isDebugEnable;

    private boolean isMaintainingDurability;
    private boolean isWorkCursorClick;
    private boolean isMaintainingVanillaEnchantment;
    private boolean isWorkCreative;
    private boolean isMaintainingAEenchant;
    private boolean isWorkJoinUpdate;
    private boolean isWorkWhitelist;
    private List<String> whitelistTypeList;


    private List<String> successSounds;

    private List<String> failSounds;

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

        Message.reloadConfigMessage(); // Message 클래스에 분리해놓은 메시진 관련 리로드 메서드

        FileConfiguration config = UpdateMMOItems.getInstance().getConfig();

        // 콘솔창에 각종 디버그 메시지를 뜨게 할 건지 옵션 (오직 op의 행동에 의해서만 로그가 출력됨)
        isDebugEnable = config.getBoolean("options.debug-log", false);

        // 아이템 업데이트 시, 깎여있던 내구도를 유지시킬건지 여부 (기본값 true)
        isMaintainingDurability = config.getBoolean("options.maintaining-durability", true);

        // 인벤토리 창에서 아이템 클릭 시, 업데이트 수행하는 옵션 (기본값 false)
        isWorkCursorClick = config.getBoolean("options.cursor-click.enable", false);

        // 업데이트 진행 시, 아이템에 발려진 바닐라 인첸트 데이터를 유지할건지 판단하는 옵션(기본값 true)
        isMaintainingVanillaEnchantment = config.getBoolean("options.maintaining-vanilla-enchantment-data", true);

        // 겜모상태에서 커서클릭 업데이트 기능 작동할지 여부를 판단하는 옵션 (기본값 false)
        isWorkCreative = config.getBoolean("options.cursor-click.work-gamemode-creative", false);

        // 업데이트 진행 시, 아이템에 발려진 AdvancedEnchantments 인첸트 데이터를 유지할건지 판단하는 옵션(기본값 true)
        isMaintainingAEenchant = config.getBoolean("options.maintaining-advanced-enchantments", true);

        // 서버 접속 시, 인벤토리에 있는 아이템들을 자동으로 업데이트 할 건지 판단하는 옵션 (기본값 false)
        isWorkJoinUpdate = config.getBoolean("options.join-update", false);

        // 업데이트 가능한 아이템 type 화이트리스트 옵션의 활성화 비활성화 여부 판단 (기본값 false)
        isWorkWhitelist = config.getBoolean("whitelist.enable", false);

        // 화이트리스트에 작성된 아이템 type 들의 값을 가져옴
        whitelistTypeList = config.getStringList("whitelist.type-list");


        successSounds = config.getStringList("sounds.success");
        failSounds = config.getStringList("sounds.fail");

    }

    public boolean getIsDebugEnable() { return isDebugEnable; }
    public boolean getIsEnableMaintainingDurability() { return isMaintainingDurability; }
    public boolean getIsWorkCursorClick() { return isWorkCursorClick; }
    public boolean getIsMaintainingVanillaEnchantment() { return isMaintainingVanillaEnchantment; }

    public boolean getIsMaintainingAEenchant() { return isMaintainingAEenchant; }
    public boolean getIsWorkCreative() { return isWorkCreative; }
    public boolean getIsWorkJoinUpdate() { return isWorkJoinUpdate; }
    public boolean getIsWorkWhitelist() { return isWorkWhitelist; }
    public List<String> getWhitelistTypeList() { return whitelistTypeList; }

    public List<String> getSuccessSounds() {
        return successSounds;
    }

    public List<String> getFailSounds() { return failSounds; }

}
