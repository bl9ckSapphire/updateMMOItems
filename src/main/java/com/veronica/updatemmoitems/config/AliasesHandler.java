package com.veronica.updatemmoitems.config;

import com.veronica.updatemmoitems.UpdateMMOItems;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class AliasesHandler {
    private static AliasesHandler instance;
    private FileConfiguration aliasesConfig;
    private boolean enableAliases;
    private List<String> aliasList;
    private final JavaPlugin plugin;

    private AliasesHandler() {
        this.plugin = UpdateMMOItems.getInstance();
        loadAliasesConfig();
    }

    public static AliasesHandler getInstance() {
        if (instance == null) instance = new AliasesHandler();
        return instance;
    }

    private void loadAliasesConfig() {
        File aliasesFile = new File(plugin.getDataFolder(), "aliases.yml");

        if (!aliasesFile.exists()) {
            plugin.saveResource("aliases.yml", false);
            plugin.getLogger().info("aliases.yml 이 존재하지 않아, 새로 생성됨.");
        }

        aliasesConfig = YamlConfiguration.loadConfiguration(aliasesFile);

        enableAliases = aliasesConfig.getBoolean("aliases.enable", true);
        aliasList = aliasesConfig.getStringList("aliases.list");
    }

    public void applyAliasesToCommand() {
        if (getIsAliasesEnabled()) {
            List<String> aliasesList = getAliasList();
            if (aliasesList != null && !aliasesList.isEmpty()) {
                unregisterCommand("updatemmoitems");
                registerCommand("updatemmoitems", aliasesList);
                getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ ChatColor.GREEN + "추가된 명령어 별칭(aliases): " + aliasesList);
            }
        }
    }

    private void unregisterCommand(String commandName) {
        try {
            final Field bukkitCommandMap = getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(getServer());
            Command command = commandMap.getCommand(commandName);
            if (command != null) {
                command.unregister(commandMap);
            }
        } catch (Exception e) {
            // 디버깅 용도
            plugin.getLogger().warning("명령어 해제 중 오류 발생: " + e.getMessage());
        }
    }

    private void registerCommand(String commandName, List<String> aliases) {
        try {
            final Field bukkitCommandMap = getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(getServer());

            Command command = plugin.getCommand(commandName);
            if (command != null) {
                command.setAliases(aliases);
                commandMap.register(plugin.getName(), command);
            } else {
                // 디버깅 용도
                plugin.getLogger().warning("명령어 '" + commandName + "'를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            // 디버깅 용도
            plugin.getLogger().warning("명령어 등록 중 오류 발생: " + e.getMessage());
        }
    }

    public boolean getIsAliasesEnabled() {
        return enableAliases;
    }

    public List<String> getAliasList() {
        return new ArrayList<>(aliasList);
    }

    public void reloadAliasesConfig() {
        loadAliasesConfig();
    }
}