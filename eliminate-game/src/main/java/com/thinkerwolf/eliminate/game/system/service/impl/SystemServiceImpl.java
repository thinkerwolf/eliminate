package com.thinkerwolf.eliminate.game.system.service.impl;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.pub.sdata.entity.MapScript;
import com.thinkerwolf.eliminate.pub.sdata.service.MapScriptService;
import com.thinkerwolf.eliminate.game.system.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("systemService")
public class SystemServiceImpl implements ISystemService {

    @Autowired
    MapScriptService mapScriptService;

    @Override
    public OpResult save(int scriptId, String content) {
        MapScript mapScript = mapScriptService.getById(scriptId);
        if (mapScript == null) {
            mapScript = new MapScript();
            mapScript.setScriptId(scriptId);
        }
        mapScript.setContent(content);
        mapScriptService.saveOrUpdate(mapScript);
        Map<String, Object> data = new HashMap<>();
        data.put("scriptId", mapScript.getScriptId());
        return OpResult.ok(data);
    }

    @Override
    public OpResult getScript(int scriptId) {
        MapScript mapScript = mapScriptService.getById(scriptId);
        if (mapScript == null) {
            return OpResult.fail("没找到地图脚本");
        }
        return OpResult.ok(mapScript);
    }
}
