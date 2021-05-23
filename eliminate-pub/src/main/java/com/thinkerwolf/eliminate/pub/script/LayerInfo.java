package com.thinkerwolf.eliminate.pub.script;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LayerInfo {

    private int height;
    private int width;
    private List<Pos> tiles;

}
