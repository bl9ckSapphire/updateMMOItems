package com.veronica.updatemmoitems;

import com.veronica.updatemmoitems.command.CommandHandler;
import com.veronica.updatemmoitems.command.sub.ReloadCommand;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.listener.InventoryClick;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class UpdateMMOItems extends JavaPlugin {

    private static Logger logger;
    private static UpdateMMOItems instance;
    private ConfigHandler configHandler;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }
    public static UpdateMMOItems getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        logger = getLogger();
        instance = this;
        configHandler = ConfigHandler.getInstance();

        // MMOItems 플러그인을 찾기
        Plugin mmoItemsPlugin = getServer().getPluginManager().getPlugin("MMOItems");

        // MMOItems 와 NBTAPI 존재 여부를 검사
        if (mmoItemsPlugin != null && mmoItemsPlugin.isEnabled() && isNBTAPIAvailable()) {
            // Plugin startup logic
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ ChatColor.GOLD + "--------------------------------------");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.GOLD + "MMOItems, NBT-API 감지됨. 활성화 완료");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.GOLD + "updateMMOItems 활성화");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.GOLD + "--------------------------------------");
        } else {
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ ChatColor.RED + "--------------------------------------------------");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.RED + "작동하려면 MMOItems, NBT-API 플러그인이 필요합니다.");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.RED + "updateMMOItems 비활성화...");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.RED + "--------------------------------------------------");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // config.yml 파일을 플러그인 폴더에 생성하는 bukkitAPI (폴더에 config.yml이 존재하지 안은 경우에만 생성)
        this.saveDefaultConfig();

        // ConfigHandler 클래스에 구현해 둔 config 정보 리로드 메서드
        ConfigHandler.getInstance().reloadConfigOptions();

        // 플러그인의 커맨드 등록
        registerCommands();

        // 이벤트 리스너 등록
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
    }

    private void registerCommands() {
        // CommandHandler 인스턴스 생성
        CommandHandler commandHandler = new CommandHandler();

        // 플러그인의 커맨드 등록
        commandHandler.registerSubCommand("reload", new ReloadCommand());

        // PluginManager에 CommandExecutor로 등록
        getCommand("updatemmoitems").setExecutor(commandHandler);
    }

    // nbtapi(NBTAPI) 는 getServer().getPluginManager().getPlugin() 으로 name 을 받아오는 과정에 하자가 있음
    // 따라서, Class.forName() 로 클래스명이 존재하는지로 검사
    private boolean isNBTAPIAvailable() {
        try {
            Class.forName("de.tr7zw.nbtapi.NBTItem");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Logger getPluginLogger() {
        return logger;
    }


}
