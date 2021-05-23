package com.thinkerwolf.eliminate.pub.battle.map;

public interface IBlock {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    int getLayer();

    String getPic();

    void setPic(String pic);
}
