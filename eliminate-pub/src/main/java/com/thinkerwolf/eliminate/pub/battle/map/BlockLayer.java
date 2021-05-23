package com.thinkerwolf.eliminate.pub.battle.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thinkerwolf.gamer.common.SymbolConstants;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 一个块层
 *
 * @author wukai
 */
public class BlockLayer {

    public static final int Y = 30;
    public static final int X = 30;
    public static final String[][] Y2X = new String[Y][X];

    static {
        for (int j = 0; j < Y; j++) {
            for (int i = 0; i < X; i++) {
                Y2X[j][i] = j + SymbolConstants.MINUS + i;
            }
        }
    }

    private static String key(int y, int x) {
        if (y > Y - 1 || x > X - 1) {
            return y + SymbolConstants.MINUS + x;
        }
        return Y2X[y][x];
    }

    private final int layer;
    private final ConcurrentMap<String, IBlock> blockMap = Maps.newConcurrentMap();
    private final int height;
    private final int width;
    @JsonIgnore
    private final float halfHeight;
    @JsonIgnore
    private final float halfWidth;

    public BlockLayer(int layer, int height, int width) {
        this.layer = layer;
        this.height = height;
        this.width = width;
        this.halfHeight = (float) height / 2;
        this.halfWidth = (float) width / 2;
    }

    public IBlock getBlock(int y, int x) {
        return blockMap.get(key(y, x));
    }

    public void setBlock(final IBlock block) {
        blockMap.put(key(block.getY(), block.getX()), block);
    }

    public IBlock removeBlock(int y, int x) {
        return blockMap.remove(key(y, x));
    }

    public int getLayer() {
        return layer;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @JsonProperty(value = "blocks")
    public Set<IBlock> getBlocks() {
        return Sets.newHashSet(blockMap.values());
    }

    @JsonIgnore
    public boolean isEmpty() {
        return blockMap.isEmpty();
    }

    public float relativeYof(int ay) {
        return ay - halfHeight + 0.5f;
    }

    public float relativeXof(int ax) {
        return ax - halfWidth + 0.5f;
    }

    public float absoluteYof(float ry) {
        if (height % 2 == 0) {
            return ry + halfHeight - 0.5f;
        }
        return ry + halfHeight - 0.5f;
    }

    public float absoluteXof(float rx) {
        if (width % 2 == 0) {
            return rx + halfWidth - 0.5f;
        }
        return rx + halfWidth - 0.5f;
    }

    public boolean exists(int y, int x) {
        return getBlock(y, x) != null;
    }

    public void destroy() {
        blockMap.clear();
    }

}
