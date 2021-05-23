package com.thinkerwolf.eliminate.pub.battle.map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 一个图标快
 *
 * @author wukai
 */
public class Block implements IBlock {
    /**
     * layer
     */
    private final int layer;
    /**
     * y
     */
    private final int y;
    /**
     * x
     */
    private final int x;
    /**
     * 块图片
     */
    private String pic;

    public Block(int layer, int y, int x, String pic) {
        this.layer = layer;
        this.y = y;
        this.x = x;
        this.pic = pic;
    }


    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getX() {
        return x;
    }

    @JsonIgnore
    @Override
    public int getWidth() {
        return 1;
    }

    @JsonIgnore
    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public String getPic() {
        return pic;
    }

    @Override
    public void setPic(String pic) {
        this.pic = pic;
    }
}
