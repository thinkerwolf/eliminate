package com.thinkerwolf.eliminate.pub.script;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.MapScript;
import com.thinkerwolf.gamer.core.mvc.model.JacksonModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MapScriptInfoFactory {
    private static final ObjectMapper mapper = JacksonModel.objectMapper;

    private static final Map<Integer, MapScriptInfo> cache = Maps.newConcurrentMap();

    public static void clear() {
        cache.clear();
    }

    public static MapScriptInfo getInfo(int scriptId) {
        return cache.computeIfAbsent(scriptId, k -> {
            MapScript mapScript = SDataBusManager.getDataGetter().getMapScriptCache().get(scriptId);
            if (mapScript == null) {
                return null;
            }
            if (StringUtils.isBlank(mapScript.getContent())) {
                return null;
            }
            ObjectReader reader = mapper.reader();
            try {
                JsonNode jo = reader.readTree(mapScript.getContent());
                String levelName = jo.get("levelName").asText();
                String random = jo.get("random").asText();
                List<String> randoms = Arrays.asList(StringUtils.split(random, '|'));
                int totalPosNum = 0;
                List<LayerInfo> layers = Lists.newArrayList();
                JsonNode  jlayers = jo.get("layers");
                for (int i = 0; i < jlayers.size(); i++) {
                    JsonNode jlayer =  jlayers.get(i);
                    String[] wh = StringUtils.split(jlayer.get("size").asText(), '-');
                    int width = NumberUtils.toInt(wh[0]);
                    int height = NumberUtils.toInt(wh[1]);
                    List<Pos> points = Lists.newArrayList();

                    JsonNode jtiles = jlayer.get("tiles");
                    for (int j = 0; j < jtiles.size(); j++) {
                        String tile = jtiles.get(j).asText();
                        wh = StringUtils.split(tile, '-');
                        Pos pos = new Pos(NumberUtils.toInt(wh[1]), NumberUtils.toInt(wh[0]));
                        points.add(pos);
                    }
                    LayerInfo layerInfo = new LayerInfo(height, width, points);
                    layers.add(layerInfo);
                    totalPosNum += points.size();
                }
                return new MapScriptInfo(scriptId, levelName, randoms, layers, totalPosNum);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

}
