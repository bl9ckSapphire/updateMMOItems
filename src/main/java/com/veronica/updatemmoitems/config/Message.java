package com.veronica.updatemmoitems.config;
import com.veronica.updatemmoitems.UpdateMMOItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.Plugin;
public enum Message {

    PREFIX("<prefix><gold>[업데이트]"),
    NO_PERMISSION("<prefix><gold>권환(펄미션) 없음"),
    NO_PLAYER("<gold><bold>[</bold><yellow>플레이어만 사용 가능한 명령어입니다."),
    IS_AIR("<gold><bold>[</bold><yellow>손에 아무것도 들고있지 않음"),
    NO_MMOITEMS("<prefix><white>올바른 아이템을 들고 시도해주세요."),
    DETECTED_GEMSTONE("<prefix><white>올바른 아이템을 들고 시도해주세요."),
    ALREADY_LATEST("<yellow>이 명령어는 오직 플레이어를 통해서만 사용 가능합니다."),
    USED_ITEM("<gold><bold>[</bold><yellow>내구도를 완전히 수리하신 후 사용해주세요."),
    NO_INVENTORY_SPACE("<gold><bold>[</bold><yellow>인벤토리에 공간이 없습니다."),
    SUCCESS_UPDATE("<white>아이템 업데이트 완료!"),
    AUTO_UPDATE_FROM_EVENT("<white>변경사항이 감지되어 해당 아이템이 업데이트 되었습니다."),
    UPDATE_JOIN_EVENT("<white>변경사항이 존재하는 인벤토리 내부의 아이템들이 업데이트됨."),
    NO_WHITELIST_ITEMS("<white>업데이트가 허용되지 않은 타입의 아이템입니다."),
    RELOAD("<prefix><gold>config 리로드 완료.");

    Component message;
    Message(String message) {
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    public String getMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.message);
    }

    private void setMessage(String message) {
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    public static void reloadConfigMessage() {
        Plugin instance = UpdateMMOItems.getInstance();

        PREFIX.setMessage(instance.getConfig().getString("message.prefix"));

        NO_PERMISSION.setMessage(instance.getConfig().getString("message.no-permission"));

        NO_PLAYER.setMessage(instance.getConfig().getString("message.no-player"));

        IS_AIR.setMessage(instance.getConfig().getString("message.is-air"));

        NO_MMOITEMS.setMessage(instance.getConfig().getString("message.no-mmoitems"));

        DETECTED_GEMSTONE.setMessage(instance.getConfig().getString("message.detected-gemstone"));

        ALREADY_LATEST.setMessage(instance.getConfig().getString("message.already-latest"));

        USED_ITEM.setMessage(instance.getConfig().getString("message.used-item"));

        NO_INVENTORY_SPACE.setMessage(instance.getConfig().getString("message.no-inv-space"));

        SUCCESS_UPDATE.setMessage(instance.getConfig().getString("message.success-update"));

        AUTO_UPDATE_FROM_EVENT.setMessage(instance.getConfig().getString("message.auto-update"));

        UPDATE_JOIN_EVENT.setMessage(instance.getConfig().getString("message.join-update"));

        NO_WHITELIST_ITEMS.setMessage(instance.getConfig().getString("message.no-whitelist-items"));

        RELOAD.setMessage(instance.getConfig().getString("message.reload"));

    }
}