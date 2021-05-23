package com.thinkerwolf.eliminate.pub.battle.map;

import com.google.common.collect.Lists;
import com.thinkerwolf.eliminate.pub.script.LayerInfo;
import com.thinkerwolf.eliminate.pub.script.MapScriptInfo;
import com.thinkerwolf.eliminate.pub.script.MapScriptInfoFactory;
import com.thinkerwolf.eliminate.pub.script.Pos;
import org.apache.commons.lang.math.RandomUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 战斗地图生成
 *
 * @author wukai
 * @since 2020-05-27
 */
public final class BattleMapFactory {

    public static BattleMap create(int scriptId) {
        MapScriptInfo msi = MapScriptInfoFactory.getInfo(scriptId);
        if (msi == null) {
            return null;
        }
        List<String> randoms = Lists.newArrayList(msi.getRandoms());
        Collections.shuffle(randoms);
        int group = msi.getTotalPosNum() / 3;

        List<GroupSize> groupSizes = Lists.newArrayList();
        for (int i = 0; i < randoms.size(); i++) {
            int num = group / randoms.size();
            GroupSize gs = new GroupSize();
            gs.num = num * 3;
            gs.pic = randoms.get(i);
            groupSizes.add(gs);
        }

        for (int i = 0; i < group % randoms.size(); i++) {
            GroupSize gs = groupSizes.get(RandomUtils.nextInt(groupSizes.size()));
            gs.num += 3;
        }

        BlockLayer[] layers = new BlockLayer[msi.getLayers().size()];
        for (int i = msi.getLayers().size() - 1; i >= 0; i--) {
            LayerInfo li = msi.getLayers().get(i);
            BlockLayer bl = new BlockLayer(i, li.getHeight(), li.getWidth());
            for (Pos pos : li.getTiles()) {
                String pic = random(groupSizes, RandomUtils.JVM_RANDOM);
                if (pic == null) {
                    break;
                }
                Block block = new Block(i, pos.getY(), pos.getX(), pic);
                bl.setBlock(block);
            }
            layers[i] = bl;
        }
        return new BattleMap(layers, randoms);
    }

    private static class GroupSize {
        int num;
        String pic;
    }

    private static String random(List<GroupSize> list, Random r) {
        if (list.isEmpty()) {
            return null;
        }
        GroupSize gs = list.get(r.nextInt(list.size()));
        gs.num--;
        if (gs.num <= 0) {
            list.remove(gs);
        }
        return gs.pic;
    }

}
