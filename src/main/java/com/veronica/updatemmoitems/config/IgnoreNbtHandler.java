package com.veronica.updatemmoitems.config;

import com.veronica.updatemmoitems.UpdateMMOItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IgnoreNbtHandler {

    private static IgnoreNbtHandler instance;
    private FileConfiguration ignoreNbtConfig;
    private List<String> ignoreVanillaNbtList = new ArrayList<>();
    private List<String> ignoreMMOItemsStatNbtList = new ArrayList<>();

    private final Plugin plugin;

    private IgnoreNbtHandler() {
        this.plugin = UpdateMMOItems.getInstance();
        loadIgnoreNbtConfig();
    }

    public static IgnoreNbtHandler getInstance() {
        if (instance == null) {
            instance = new IgnoreNbtHandler();
        }
        return instance;
    }

    private void loadIgnoreNbtConfig() {
        File ignoreNbtFile = new File(plugin.getDataFolder(), "ignoreNBT.yml");

        if (!ignoreNbtFile.exists()) {
            plugin.saveResource("ignoreNBT.yml", false);
            plugin.getLogger().info("ignoreNBT.yml 파일이 존재하지 않아, 새로 생성됨.");
        }

        ignoreNbtConfig = YamlConfiguration.loadConfiguration(ignoreNbtFile);

        // 데이터 로드 전 기존 리스트 초기화
        ignoreVanillaNbtList.clear();
        ignoreMMOItemsStatNbtList.clear();

        ignoreVanillaNbtList = ignoreNbtConfig.getStringList("Ignore-NBT.vanilla-nbt-list");
        ignoreMMOItemsStatNbtList = ignoreNbtConfig.getStringList("Ignore-NBT.mmoitems-custom-stat-nbt-list");

    }

    public void reloadIgnoreNbtConfig() { loadIgnoreNbtConfig(); }

    public List<String> getIgnoreVanillaNbtList() { return ignoreVanillaNbtList; }

    public List<String> getIgnoreMMOItemsStatNbtList() { return ignoreMMOItemsStatNbtList; }
}
