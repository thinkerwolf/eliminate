package com.thinkerwolf.eliminate.pub.sdata;

import com.thinkerwolf.eliminate.pub.sdata.cache.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SDataBus {
    @Autowired
    private BuildingCache buildingCache;

    @Autowired
    private BuildingLvCache buildingLvCache;

    @Autowired
    private TaskCache taskCache;

    @Autowired
    private MapScriptCache mapScriptCache;

    @Autowired
    private StageCache stageCache;

    @Autowired
    private MiscCache miscCache;

    @Autowired
    private DayGiftCache dayGiftCache;

    @Autowired
    private CharacterActionCache characterActionCache;

    @Autowired
    private TalkCache talkCache;

    @Autowired
    private TalkPopCache talkPopCache;
}
