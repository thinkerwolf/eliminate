package com.thinkerwolf.eliminate.game.building.action;

import com.thinkerwolf.eliminate.game.building.service.IPlayerBuildingService;
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
        value = "building@",
        views = {
                @View(name = "json", type = JsonView.class),
        })
@Api(tags = {"building"}, description = "建筑相关接口")
public class PlayerBuildingAction extends BaseAction {

    @Autowired
    IPlayerBuildingService playerBuildingService;

    @Command("upgrade")
    @ApiOperation(value = "升级或者建造建筑", notes = "升级或者建造建筑")
    public JsonModel buildingUpgrade(Request request, @RequestParam("buildingId") int buildingId) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(playerBuildingService.upgrade(playerDto.getPlayerId(), buildingId), request);
    }

    @Command("confirm")
    @ApiOperation(value = "建筑升级完确认", notes = "建筑升级完确认")
    public JsonModel confirm(Request request, @RequestParam("buildingId") int buildingId) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(playerBuildingService.confirm(playerDto.getPlayerId(), buildingId), request);
    }


}
