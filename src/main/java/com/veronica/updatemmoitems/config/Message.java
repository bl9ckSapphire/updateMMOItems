package com.veronica.updatemmoitems.config;
import com.veronica.updatemmoitems.UpdateMMOItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.Plugin;
public enum Message {

    PREFIX("<prefix><gold>[업데이트]"),
    NO_PERMISSION("<prefix><gold>권환(펄미션) 없음"),
    NO_PLAYER("<prefix><white>플레이어만 사용 가능한 명령어입니다."),
    IS_AIR("<prefix><white>손에 아무것도 들고있지 않음"),
    NO_INCORRECT_ITEMS("<prefix><white>올바른 아이템을 들고 시도해주세요."),
    UPGRADABLE_ITEMS("<prefix><white>현재, 업그레이드 가능한 아이템은 업데이트 할 수 없도록 설정되어있습니다."),
    DETECTED_GEMSTONE("<prefix><white>올바른 아이템을 들고 시도해주세요."),
    ALREADY_LATEST("<prefix><yellow>이미 최신화된 아이템입니다."),
    USED_ITEM("<prefix><white>내구도를 완전히 수리하신 후 사용해주세요."),
    NO_INVENTORY_SPACE("<prefix><white>인벤토리에 공간이 없습니다."),
    SUCCESS_UPDATE("<prefix><white>아이템 업데이트 완료!"),
    CURSOR_CLICK_UPDATE("<prefix><white>변경사항이 감지되어 해당 아이템이 업데이트 되었습니다."),
    NO_WHITELIST_ITEMS("<prefix><white>업데이트가 허용되지 않은 타입의 아이템입니다."),

    INVENTORY_UPDATE_SELF("<prefix><white>변경사항이 존재하는 인벤토리 내부의 아이템들이 업데이트됨."),
    INVENTORY_UPDATE_OTHER("<prefix><yellow><player><white>님의 인벤토리에 업데이트 되지않은 <yellow><amount><white>개의 아이템을 모두 업데이트 시켰습니다."),
    INVENTORY_ALREADY_ALL_UPDATED("<prefix><white>이미 인벤토리의 모든 아이템이 최신 상태입니다."),
    INVENTORY_ALREADY_ALL_UPDATED_OTHER("<prefix><yellow><player><white>님은 이미 인벤토리의 모든 아이템이 최신 상태입니다."),
    INVENTORY_UPDATE_NOTIFICATION("<prefix><white>관리자에 의해, 내 인벤토리에 있는 <yellow><amount><white>개의 아이템이 업데이트 되었습니다."),

    INVALID_PLAYER("<prefix><yellow><player><white>은(는) 존재하지 않는 닉네임입니다.."),
    INVALID_ARGUMENTS("<prefix><white>잘못된 인수입니다.."),
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

        NO_INCORRECT_ITEMS.setMessage(instance.getConfig().getString("message.no-incorrect-items"));

        UPGRADABLE_ITEMS.setMessage(instance.getConfig().getString("message.upgrading-items"));

        DETECTED_GEMSTONE.setMessage(instance.getConfig().getString("message.detected-gemstone"));

        ALREADY_LATEST.setMessage(instance.getConfig().getString("message.already-latest"));

        USED_ITEM.setMessage(instance.getConfig().getString("message.used-item"));

        NO_INVENTORY_SPACE.setMessage(instance.getConfig().getString("message.no-inv-space"));

        SUCCESS_UPDATE.setMessage(instance.getConfig().getString("message.success-update"));

        CURSOR_CLICK_UPDATE.setMessage(instance.getConfig().getString("message.cursor-click-update"));

        INVENTORY_UPDATE_SELF.setMessage(instance.getConfig().getString("message.inv-update-self"));

        INVENTORY_UPDATE_OTHER.setMessage(instance.getConfig().getString("message.inv-update-other"));

        INVENTORY_ALREADY_ALL_UPDATED.setMessage(instance.getConfig().getString("message.inv-already-all-updated"));

        INVENTORY_ALREADY_ALL_UPDATED_OTHER.setMessage(instance.getConfig().getString("message.inv-already-all-updated-other"));

        INVENTORY_UPDATE_NOTIFICATION.setMessage(instance.getConfig().getString("message.inv-update-notification"));

        NO_WHITELIST_ITEMS.setMessage(instance.getConfig().getString("message.no-whitelist-items"));

        INVALID_PLAYER.setMessage(instance.getConfig().getString("message.invaild-player"));

        INVALID_ARGUMENTS.setMessage(instance.getConfig().getString("message.invaild-args"));

        RELOAD.setMessage(instance.getConfig().getString("message.reload"));

    }
}