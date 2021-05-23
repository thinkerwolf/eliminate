package com.thinkerwolf.eliminate.game.player.action;

import com.thinkerwolf.eliminate.game.player.service.IPlayerService;
import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.gamer.core.annotation.Action;
import com.thinkerwolf.gamer.core.annotation.Command;
import com.thinkerwolf.gamer.core.annotation.RequestParam;
import com.thinkerwolf.gamer.core.annotation.View;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.mvc.view.HtmlView;
import com.thinkerwolf.gamer.core.mvc.view.JsonView;
import com.thinkerwolf.gamer.core.servlet.Request;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

@Action(views = {
        @View(name = "byte", type = JsonView.class),
        @View(name = "freemarker", type = HtmlView.class),
        @View(name = "json", type = JsonView.class),
})
@Api(tags = {"player"}, description = "玩家操作接口")
public class PlayerAction extends BaseAction {

    @Autowired
    IPlayerService playerService;

    @Command("player@login")
    @ApiOperation(value = "玩家登录")
    public JsonModel loginPlayer(Request request, @RequestParam("playerId") int playerId) {
        OpResult op = playerService.loginPlayer(request, playerId);
        return createJsonModel(op, request);
    }

    @Command("player@gwLogin")
    @ApiOperation(value = "玩家网关登录")
    public JsonModel gwLogin(Request request, @RequestParam("playerId") int playerId) {
        OpResult op = playerService.gwLogin(request, playerId);
        return createJsonModel(op, request);
    }
}
