package com.thinkerwolf.eliminate.databus;

import com.thinkerwolf.eliminate.bag.service.IPlayerBagService;
import com.thinkerwolf.eliminate.building.service.IPlayerBuildingService;
import com.thinkerwolf.eliminate.diary.service.IDiaryTaskService;
import com.thinkerwolf.eliminate.diary.service.IPlayerDayService;
import com.thinkerwolf.eliminate.diary.service.IPlayerDiaryTaskService;
import com.thinkerwolf.eliminate.player.service.IPlayerService;
import com.thinkerwolf.eliminate.stage.service.IPlayerStageService;
import com.thinkerwolf.gamer.core.servlet.ServletBootstrap;
import com.thinkerwolf.gamer.registry.Registry;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DataBus {

    @Autowired
    private ServletBootstrap servletBootstrap;

    @Autowired
    private IPlayerBuildingService playerBuildingService;

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IPlayerDiaryTaskService playerTaskService;

    @Autowired
    private IDiaryTaskService diaryTaskService;

    @Autowired
    private IPlayerStageService playerStageService;

    @Autowired
    private IPlayerBagService playerBagService;

    @Autowired
    private IPlayerDayService playerDayService;

    @Autowired(required = false)
    private Registry registry;
}
