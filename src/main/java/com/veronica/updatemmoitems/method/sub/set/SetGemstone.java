package com.veronica.updatemmoitems.method.sub.set;

import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.stat.data.GemSocketsData;
import net.Indyuce.mmoitems.stat.data.GemstoneData;
import net.Indyuce.mmoitems.stat.data.type.Mergeable;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.GemStoneStat;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SetGemstone {




    public static ItemStack setGemstoneData(LiveMMOItem targetLiveMMOItem, MMOItem latestMMOItem) {
        // latestMMOItem을 LiveMMOItem으로 변환
        LiveMMOItem liveLatestMMOItems = new LiveMMOItem(latestMMOItem.newBuilder().build());

        // targetLiveMMOItem의 gemstone 정보 가져오기
        GemSocketsData targetGemSocketsData = (GemSocketsData) targetLiveMMOItem.getData(ItemStats.GEM_SOCKETS);
        GemSocketsData latestGemSocketsData = (GemSocketsData) liveLatestMMOItems.getData(ItemStats.GEM_SOCKETS);

        if (targetGemSocketsData != null && latestGemSocketsData != null) {
            // targetLiveMMOItem의 젬스톤들을 liveLatestMMOItems에 적용
            for (GemstoneData gemstone : targetGemSocketsData.getGemstones()) {
                String gemColor = gemstone.getSocketColor();

                // 일치하는 빈 슬롯 찾기
                String matchingSocket = null;
                for (String emptySlot : latestGemSocketsData.getEmptySlots()) {
                    if (emptySlot.equalsIgnoreCase(gemColor)) {
                        matchingSocket = emptySlot;
                        break;
                    }
                }

                if (matchingSocket != null) {
                    // 빈 슬롯에서 제거
                    latestGemSocketsData.getEmptySlots().remove(matchingSocket);

                    // 젬스톤 추가
                    latestGemSocketsData.add(gemstone);

                    // 젬스톤 스탯 적용
                    LiveMMOItem gemMMOItem = new LiveMMOItem(MMOItems.plugin.getItem(gemstone.getMMOItemType(), gemstone.getMMOItemID()));
                    for (ItemStat stat : gemMMOItem.getStats()) {
                        if (!(stat instanceof GemStoneStat)) {
                            StatData data = gemMMOItem.getData(stat);
                            if (data instanceof Mergeable) {
                                liveLatestMMOItems.mergeData(stat, data, gemstone.getHistoricUUID());
                            }
                        }
                    }
                }
            }

            // 업데이트된 GemSocketsData를 liveLatestMMOItems에 설정
            liveLatestMMOItems.setData(ItemStats.GEM_SOCKETS, latestGemSocketsData);
        }

        // 변경된 liveLatestMMOItems를 ItemStack으로 변환하여 반환
        return liveLatestMMOItems.newBuilder().build();
    }






}
