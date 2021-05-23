package com.thinkerwolf.eliminate.gm.action;

import com.thinkerwolf.eliminate.gm.comm.GmCommand;
import com.thinkerwolf.eliminate.gm.service.IGmService;
import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.eliminate.common.OpResult;
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
import org.springframework.beans.factory.annotation.Value;

@Action(views = {
        @View(name = "byte", type = JsonView.class),
        @View(name = "json", type = JsonView.class),
})
@Api(tags = {"gm"}, description = "GM操作接口")
public class GmAction extends BaseAction {

    private static final String[] EMPTY = new String[]{""};

    @Autowired
    IGmService gmService;

    @Value("${project.gm.enabled:true}")
    private boolean gmEnabled;

    @Command("gm@handle")
    @ApiOperation(value = "GM处理")
    public JsonModel handle(Request request, @RequestParam("cmd") String cmd, @RequestParam("param") String param) {
        if (!gmEnabled) {
            return createJsonModel(OpResult.fail("Gm closed"), request);
        }
        PlayerDto playerDto = getPlayerDto(request);
        GmCommand command = GmCommand.cmdOf(cmd);
        if (command == null) {
            return createJsonModel(OpResult.fail("No command [" + cmd + "]"), request);
        }
        final String[] ps = param == null ? EMPTY : param.trim().split(" ");
        OpResult op = null;
        switch (command) {
            case HELP:
                op = gmService.help();
                break;
            case REWARD:
                op = gmService.sendReward(playerDto.getPlayerId(), ps[0]);
                break;
            case CONSUME_REWARD:
                op = gmService.consumeReward(playerDto.getPlayerId(), ps[0]);
                break;
            case STOP_BATTLE:
                op = gmService.stopBattle(playerDto.getPlayerId());
                break;
            case REFRESH_SDATA:
                op = gmService.refreshSdata(playerDto.getPlayerId(), ps);
                break;
            case ACTION:
                op = gmService.action(playerDto.getPlayerId(), ps);
                break;
        }
        if (op == null) {
            op = OpResult.fail("cmd：" + cmd + " 不存在");
        }
        return createJsonModel(op, request);
    }


}
