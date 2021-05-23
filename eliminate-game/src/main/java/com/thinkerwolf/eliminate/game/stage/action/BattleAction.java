package com.thinkerwolf.eliminate.game.stage.action;

import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.game.stage.service.IBattleService;
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
@Api(tags = {"battle"}, description = "战斗相关操作")
public class BattleAction extends BaseAction {

    @Autowired
    IBattleService battleService;

    @Command("battle@begin")
    @ApiOperation(value = "开启一场新战斗")
    public JsonModel begin(Request request) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(battleService.begin(playerDto.getPlayerId()), request);
    }

    @Command("battle@start")
    @ApiOperation(value = "开启一场新战斗")
    public JsonModel start(Request request, @RequestParam("stageId") int stageId) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(battleService.start(playerDto.getPlayerId(), stageId), request);
    }

    @Command("battle@view")
    @ApiOperation(value = "查看战场情况")
    public JsonModel view(Request request, @RequestParam("battleId") String battleId) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(battleService.view(playerDto.getPlayerId(), battleId), request);
    }

    @Command("battle@takeBlock")
    @ApiOperation(value = "取出一块图形")
    public JsonModel takeBlock(Request request, @RequestParam("layer") int layer, @RequestParam("y") int y, @RequestParam("x") int x) {
        PlayerDto playerDto = getPlayerDto(request);
        OpResult op = battleService.takeBlock(playerDto.getPlayerId(), layer, y, x);
        return createJsonModel(op, request);
    }

    @Command("battle@useProp")
    @ApiOperation(value = "使用战斗道具")
    public JsonModel useProp(Request request, @RequestParam("propId") int propId) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(battleService.useProp(playerDto.getPlayerId(), propId), request);
    }

    @Command("battle@restart")
    public JsonModel restart(Request request) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(battleService.restart(playerDto.getPlayerId()), request);
    }

}
