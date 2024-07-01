package com.veronica.updatemmoitems;

import com.veronica.updatemmoitems.command.CommandHandler;
import com.veronica.updatemmoitems.command.sub.ReloadCommand;
import com.veronica.updatemmoitems.config.ConfigHandler;
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

        if (mmoItemsPlugin != null && mmoItemsPlugin.isEnabled()) {
            // Plugin startup logic
            getServer().getConsoleSender().sendMessage("[UpdateMMOItems] "+ ChatColor.GOLD + "---------------------------------------");
            getServer().getConsoleSender().sendMessage("[UpdateMMOItems] "+ChatColor.GOLD + "손에 든 MMOItems 업데이트 플러그인 활성화");
            getServer().getConsoleSender().sendMessage("[UpdateMMOItems] "+ChatColor.GOLD + "---------------------------------------");
            getLogger().info("MMOItems 감지됨. 활성화 완료");
        } else {
            getServer().getConsoleSender().sendMessage("[UpdateMMOItems] "+ ChatColor.RED + "---------------------------------------------------");
            getServer().getConsoleSender().sendMessage("[UpdateMMOItems] "+ChatColor.RED + "서버에 MMOItems 플러그인이 설치되지 않았습니다. 비활성화됨");
            getServer().getConsoleSender().sendMessage("[UpdateMMOItems] "+ChatColor.RED + "---------------------------------------------------");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }



        // config.yml 파일을 플러그인 폴더에 생성하는 bukkitAPI (폴더에 config.yml이 존재하지 안은 경우에만 생성)
        this.saveDefaultConfig();

        // ConfigHandler 클래스에 구현해 둔 config 정보 리로드 메서드
        ConfigHandler.getInstance().reloadConfigOptions();

        // 플러그인의 커맨드 등록
        registerCommands();
    }

    private void registerCommands() {
        // CommandHandler 인스턴스 생성
        CommandHandler commandHandler = new CommandHandler();

        // 플러그인의 커맨드 등록
        commandHandler.registerSubCommand("reload", new ReloadCommand());

        // PluginManager에 CommandExecutor로 등록
        getCommand("updatemmoitems").setExecutor(commandHandler);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Logger getPluginLogger() {
        return logger;
    }


}
