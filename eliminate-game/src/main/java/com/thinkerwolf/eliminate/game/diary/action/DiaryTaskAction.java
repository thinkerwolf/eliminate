package com.thinkerwolf.eliminate.game.diary.action;

import com.thinkerwolf.eliminate.game.diary.service.IDiaryTaskService;
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
        })
@Api(tags = {"diary2"}, description = "玩家日记本任务相关(new)接口")
public class DiaryTaskAction extends BaseAction {

    @Autowired
    IDiaryTaskService diaryTaskService;

    @Command("diary@curTasks")
    @ApiOperation(value = "获取当前日记本列表和进度")
    public JsonModel curTasks(Request request) {
        PlayerDto playerDto = getPlayerDto(request);
        if (playerDto == null) {
            return null;
        }
        return createJsonModel(diaryTaskService.curTasks(playerDto.getPlayerId()), request);
    }

    @Command("diary@finish")
    @ApiOperation(value = "完成任务")
    public JsonModel finish(Request request, @RequestParam("taskId") int taskId) {
        PlayerDto playerDto = getPlayerDto(request);
        if (playerDto == null) {
            return null;
        }
        return createJsonModel(diaryTaskService.finish(playerDto.getPlayerId(), taskId), request);
    }

    @Command(value = "diary@takeReward", enabled = false)
    @ApiOperation(value = "领取日记本进度奖励")
    public JsonModel takeReward(Request request, @RequestParam("idx") int idx) {
        PlayerDto playerDto = getPlayerDto(request);
        if (playerDto == null) {
            return null;
        }
        return createJsonModel(diaryTaskService.takeReward(playerDto.getPlayerId(), idx), request);
    }

}
