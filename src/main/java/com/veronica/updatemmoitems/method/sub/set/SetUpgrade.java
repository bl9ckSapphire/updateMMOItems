package com.veronica.updatemmoitems.method.sub.set;

import net.Indyuce.mmoitems.api.UpgradeTemplate;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.inventory.ItemStack;

public class SetUpgrade {

    // 업그레이드 데이터까지 적용한 LiveMMOItem 자료형의 최신 아이템을, ItemStack으로 반환하는 메서드
    public static ItemStack setUpgradeData(LiveMMOItem targetLiveMMOItem, MMOItem latestMMOItem){



        // MMOItem 자료형의 최신 아이템 데이터를 >> ItemStack 으로 변환
        ItemStack itemStack = latestMMOItem.newBuilder().build();

        // ItemStack을 사용하여, 최신 아이템 데이터를 LiveMMOItem 자료형 변수로 변환
        LiveMMOItem liveLatestMMOItems = new LiveMMOItem(itemStack);

        // MMOItem 자료형인 latestMMOItems 에 바로 업그레이드를 적용시켜 버리면,
        // 서버 자체에 등록되어있는 MMOItem 에 업그레이드를 적용시켜버려서,
        // mi browser 로 열었을 때 보이는 아이템 목록에도 강화가 적용되버리는 참사가 일어남
        // 따라서, LiveMMOItem 자료형으로 한번 더 변환한 liveLatestMMOItems 변수 선언
        // =======================================================================
        // 즉, upgradeTo() 처럼 어떤 스탯을 set 하는 코드를 사용할 때는,
        // MMOItem 자료형의 변수에는 절대 사용하면 안되고
        // LiveMMOItem 자료형의 변수에 사용해야 함



        // 타깃 아이템이 업그레이드 템플릿이 존재 + 타깃 아이템이 업그레이드가 적용된 상태라면
        if (targetLiveMMOItem.hasUpgradeTemplate() && targetLiveMMOItem.getUpgradeLevel() != 0){

            // 서버에 등록된 최신 MMOItems 에도 업그레이드 템플릿이 존재한다면
            if(liveLatestMMOItems.hasUpgradeTemplate()){

                // 서버에 등록된 최신 MMOItems 의 업그레이드 템플릿을 가져옴
                UpgradeTemplate latestMMOItemsUpgradeTemplate = liveLatestMMOItems.getUpgradeTemplate();

                // 최신 아이템인 latestMMOItems 에 << 타깃 아이템의 업그레이드 레벨 값만큼, 똑같은 레벨의 업그레이드 적용
                latestMMOItemsUpgradeTemplate.upgradeTo(liveLatestMMOItems, targetLiveMMOItem.getUpgradeLevel());

                // 이 모든 과정을 거친 latestMMOItems 를 ItemStack으로 전환하고 이것을 반환
                ItemStackBuilder builder = liveLatestMMOItems.newBuilder();
                ItemStack latestMMOItemsToItemStack = builder.build();

                return latestMMOItemsToItemStack;
            }
        }
        // 타깃 아이템이 업그레이드 템플릿이 없거나, 0강 상태일 때
        return null;
    }
}
