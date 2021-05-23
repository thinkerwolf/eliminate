package com.thinkerwolf.eliminate.pub.battle.map;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * 战斗地图
 *
 * @author wukai
 */
public class BattleMap {

    private final BlockLayer[] layers;
    private final List<String> pics;

    public BattleMap(BlockLayer[] layers, List<String> pics) {
        this.layers = layers;
        this.pics = pics;
    }

    public BlockLayer getLayer(int layer) {
        if (layer < 0 || layer >= layers.length) {
            return null;
        }
        return layers[layer];
    }

    public boolean isEmpty() {
        for (BlockLayer bl : layers) {
            if (!bl.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个块是否被上层覆盖了
     *
     * @param layer 层数
     * @param y     y
     * @param x     x
     * @return 是否被覆盖
     */
    public boolean isCovered(int layer, int y, int x) {
        BlockLayer upLayer = getLayer(layer + 1);
        if (upLayer == null || upLayer.isEmpty()) {
            return false;
        }
        BlockLayer curLayer = getLayer(layer);
        if (!curLayer.exists(y, x)) {
            return false;
        }
        float ry = curLayer.relativeYof(y);
        float rx = curLayer.relativeXof(x);

        float ay = upLayer.absoluteYof(ry);
        float ax = upLayer.absoluteXof(rx);
        int ty = (int) Math.floor(ay);
        int tx = (int) Math.floor(ax);
        for (int j = ty, mj = ty + (ty < ay ? 1 : 0); j <= mj; j++) {
            if (j < 0 || j > upLayer.getHeight()) {
                continue;
            }
            for (int i = tx, mi = tx + (tx < ax ? 1 : 0); i <= mi; i++) {
                if (i < 0 || i > upLayer.getWidth()) {
                    continue;
                }
                if (upLayer.getBlock(j, i) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public BlockLayer[] getLayers() {
        return layers;
    }

    public List<String> getPics() {
        return pics;
    }

    public void removeBlocks(Set<IBlock> bs) {
        for (IBlock b : bs) {
            BlockLayer bl = getLayer(b.getLayer());
            bl.removeBlock(b.getY(), b.getX());
        }
    }

    /**
     * 随机打乱
     */
    public void shuffleBlocks() {
        LinkedList<String> remainPics = Lists.newLinkedList();
        for (int i = layers.length - 1; i >= 0; --i) {
            BlockLayer bl = layers[i];
            for (IBlock b : bl.getBlocks()) {
                remainPics.add(b.getPic());
            }
        }
        Collections.shuffle(remainPics);
        for (int i = layers.length - 1; i >= 0; --i) {
            BlockLayer bl = layers[i];
            for (IBlock b : bl.getBlocks()) {
                b.setPic(remainPics.poll());
            }
        }
    }

    public Set<IBlock> findBlocks(String pic, int num) {
        if (num <= 0) {
            return Collections.emptySet();
        }
        Set<IBlock> bs = Sets.newHashSet();
        for (int i = layers.length - 1; i >= 0; --i) {
            BlockLayer bl = layers[i];
            for (IBlock b : bl.getBlocks()) {
                if (pic.equals(b.getPic()) && !isCovered(i, b.getY(), b.getX())) {
                    bl.removeBlock(b.getY(), b.getX());
                    bs.add(b);
                    if (--num <= 0) {
                        break;
                    }
                }
            }
            if (num <= 0) {
                break;
            }
        }
        for (IBlock b : bs) {
            getLayer(b.getLayer()).setBlock(b);
        }
        return bs;
    }

    public void destroy() {
        for (int i = 0; i < layers.length; i++) {
            if (layers[i] != null) {
                layers[i].destroy();
                layers[i] = null;
            }
        }
    }
}
