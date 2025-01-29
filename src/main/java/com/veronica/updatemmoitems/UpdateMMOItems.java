package com.veronica.updatemmoitems;

import com.veronica.updatemmoitems.command.CommandHandler;
import com.veronica.updatemmoitems.command.sub.InvUpdateCommand;
import com.veronica.updatemmoitems.command.sub.ReloadCommand;
import com.veronica.updatemmoitems.config.AliasesHandler;
import com.veronica.updatemmoitems.config.ConfigHandler;
import com.veronica.updatemmoitems.config.IgnoreNbtHandler;
import com.veronica.updatemmoitems.listener.InventoryClickEvent;
import com.veronica.updatemmoitems.listener.JoinEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public final class UpdateMMOItems extends JavaPlugin {

    private static UpdateMMOItems instance;
    private ConfigHandler configHandler;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }
    public static UpdateMMOItems getInstance() {
        return instance;
    }

    public static Boolean isEnableOraxenPlugin = false;
    public static Boolean isEnableNexoPlugin = false;
    public static Boolean isEnableItemsAdderPlugin = false;


    @Override
    public void onEnable() {

        instance = this;
        configHandler = ConfigHandler.getInstance();

        // 서버에 MMOItems, AE, Nexo, ItemsAdder 플러그인 찾기
        Plugin nexoPlugin = getServer().getPluginManager().getPlugin("Nexo");
        Plugin oraxenPlugin = getServer().getPluginManager().getPlugin("Oraxen");
        Plugin itemsAdderPlugin = getServer().getPluginManager().getPlugin("ItemsAdder");
        Plugin aePlugin = getServer().getPluginManager().getPlugin("AdvancedEnchantments");
        Plugin mmoItemsPlugin = getServer().getPluginManager().getPlugin("MMOItems");

        // 외부 플러그인 및 종속성 플러그인 존재 여부를 검사
        if (mmoItemsPlugin != null && mmoItemsPlugin.isEnabled() && isNBTAPIAvailable()) {
            // Plugin startup logic
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ ChatColor.GOLD + "--------------------------------------");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.GOLD + "MMOItems, NBT-API 감지됨. 활성화 완료");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.GOLD + "updateMMOItems 활성화");


            if (nexoPlugin != null && nexoPlugin.isEnabled()) {
                getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.AQUA + "부가적으로, Nexo 감지됨.");
                isEnableNexoPlugin = true;
            }
            if (oraxenPlugin != null && oraxenPlugin.isEnabled()) {
                getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.AQUA + "부가적으로, Oraxen 감지됨.");
                isEnableOraxenPlugin = true;
            }
            if (itemsAdderPlugin != null && itemsAdderPlugin.isEnabled()) {
                getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.AQUA + "부가적으로, ItemsAdder 감지됨.");
                isEnableItemsAdderPlugin = true;
            }
            if (aePlugin != null && aePlugin.isEnabled()) {
                getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.AQUA + "부가적으로, AdvancedEnchantments 감지됨.");
            }

            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.GOLD + "--------------------------------------");


        } else {
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ ChatColor.RED + "--------------------------------------------------");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.RED + "작동하려면 MMOItems, NBT-API 플러그인이 필요합니다.");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.RED + "updateMMOItems 비활성화...");
            getServer().getConsoleSender().sendMessage("[updateMMOItems] "+ChatColor.RED + "--------------------------------------------------");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }



        // updatemmoitems 는 plugin.yml 에 작성된 command 와 동일하게 되어야 함
        Objects.requireNonNull(this.getCommand("updatemmoitems")).setExecutor(new CommandHandler());

        // config.yml 파일을 플러그인 폴더에 생성하는 bukkitAPI (폴더에 config.yml이 존재하지 안은 경우에만 생성)
        this.saveDefaultConfig();

        // 플러그인의 커맨드 등록
        registerCommands();

        // ConfigHandler 클래스에 구현해 둔 config.yml 정보 리로드
        ConfigHandler.getInstance().reloadConfigOptions();

        // AliasesHandler 클래스에 구현해 둔 aliases.yml 정보 리로드 및 명령어 별칭으로 서버에 등록
        AliasesHandler.getInstance().reloadAliasesConfig();
        AliasesHandler.getInstance().applyAliasesToCommand();


        // IgnoreNbtHandler 클래스에 구현해 준 ignoreNbt.yml 정보 리로드
        IgnoreNbtHandler.getInstance().reloadIgnoreNbtConfig();

        // 이벤트 리스너 등록
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
    }

    private void registerCommands() {
        // 탭 자동완성 리스트 등록
        CommandHandler.subcommandList.put("reload", new ReloadCommand());
        CommandHandler.subcommandList.put("인벤토리", new InvUpdateCommand());

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



}
