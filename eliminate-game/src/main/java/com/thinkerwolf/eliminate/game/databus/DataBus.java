package com.thinkerwolf.eliminate.game.databus;

import com.thinkerwolf.eliminate.game.bag.service.IPlayerBagService;
import com.thinkerwolf.eliminate.game.building.service.IPlayerBuildingService;
import com.thinkerwolf.eliminate.game.diary.service.IDiaryTaskService;
import com.thinkerwolf.eliminate.game.diary.service.IPlayerDayService;
import com.thinkerwolf.eliminate.game.diary.service.IPlayerDiaryTaskService;
import com.thinkerwolf.eliminate.game.player.service.IPlayerService;
import com.thinkerwolf.eliminate.game.stage.service.IPlayerStageService;
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
