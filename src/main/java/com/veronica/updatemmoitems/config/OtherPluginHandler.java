package com.veronica.updatemmoitems.config;

import com.veronica.updatemmoitems.UpdateMMOItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class OtherPluginHandler {

    private static OtherPluginHandler instance;
    private FileConfiguration otherPluginsConfig;
    private boolean isEnableOraxenItems;
    private boolean isEnableItemsAdderItems;


    private final JavaPlugin plugin;

    private OtherPluginHandler() {
        this.plugin = UpdateMMOItems.getInstance();
        loadOtherPluginsConfig();
    }

    public static OtherPluginHandler getInstance() {
        if (instance == null) instance = new OtherPluginHandler();
        return instance;
    }

    private void loadOtherPluginsConfig() {
        File otherPluginsFile = new File(plugin.getDataFolder(), "otherPlugins.yml");

        if (!otherPluginsFile.exists()) {
            plugin.saveResource("otherPlugins.yml", false);
            plugin.getLogger().info("otherPlugins.yml 이 존재하지 않아, 새로 생성됨.");
        }

        otherPluginsConfig = YamlConfiguration.loadConfiguration(otherPluginsFile);

        // 서버에 oraxen, ia 플러그인이 있는지 변수에 대입
        Plugin itemsAdderPlugin = getServer().getPluginManager().getPlugin("ItemsAdder");
        Plugin oraxenPlugin = getServer().getPluginManager().getPlugin("Oraxen");

        // ItemsAdder 플러그인이 서버에 존재하지 않을경우 false 반환
        // 여기서, null 인지 먼저 확인하고, isEnabled() 을 확인해야 NPE 문제가 발생하지 않음
        if( itemsAdderPlugin == null || !itemsAdderPlugin.isEnabled() ){ isEnableItemsAdderItems = false; }
        // 존재할 경우 콘피그 경로에서 값을 받아옴
        else { isEnableItemsAdderItems = otherPluginsConfig.getBoolean("ItemsAdder.enable", false); }



        // Oraxen 플러그인이 서버에 존재하지 않을경우 false 반환
        // 여기서, null 인지 먼저 확인하고, isEnabled() 을 확인해야 NPE 문제가 발생하지 않음
        if( oraxenPlugin == null || !oraxenPlugin.isEnabled() ) { isEnableOraxenItems = false; }
        // Oraxen 이 존재할 경우 콘피그 경로에서 값을 받아옴
        else { isEnableOraxenItems = otherPluginsConfig.getBoolean("Oraxen.enable", false); }


    }

    public boolean getIsEnableItemsAdderItems(){ return isEnableItemsAdderItems; }
    public boolean getIsEnableOraxenItems(){ return isEnableOraxenItems; }

    public void reloadOtherPluginsConfig() {
        loadOtherPluginsConfig();
    }


}
