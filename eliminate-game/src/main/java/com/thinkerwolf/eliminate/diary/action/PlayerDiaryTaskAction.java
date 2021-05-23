package com.thinkerwolf.eliminate.diary.action;

import com.thinkerwolf.eliminate.diary.service.IPlayerDiaryTaskService;
import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.gamer.core.annotation.Action;
import com.thinkerwolf.gamer.core.annotation.Command;
import com.thinkerwolf.gamer.core.annotation.RequestParam;
import com.thinkerwolf.gamer.core.annotation.View;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.mvc.view.JsonView;
import com.thinkerwolf.gamer.core.servlet.Request;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

@Action(
        views = {
                @View(name = "json", type = JsonView.class),
        }, enabled = false)
@Api(tags = {"diary"}, description = "玩家日记本任务相关接口")
public class PlayerDiaryTaskAction extends BaseAction {

    @Autowired
    IPlayerDiaryTaskService playerTaskService;

    @Command("diary@dayTasks")
    @ApiOperation(value = "获取日记本内容")
    public JsonModel getDayTasks(Request request) {
        PlayerDto playerDto = getPlayerDto(request);
        if (playerDto == null) {
            return null;
        }
        return createJsonModel(playerTaskService.getDayTasks(playerDto.getPlayerId()), request);
    }

}
