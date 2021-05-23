package com.thinkerwolf.eliminate.pub.script;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MapScriptInfo {

    private int scriptId;
    private String levelName;
    private List<String> randoms;
    private List<LayerInfo> layers;
    private int totalPosNum;

    /*public MapScriptInfo(int scriptId, String levelName, List<String> randoms, List<LayerInfo> layers) {
        this.scriptId = scriptId;
        this.levelName = levelName;
        this.randoms = randoms;
        this.layers = layers;
    }*/



}
