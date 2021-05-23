package com.thinkerwolf.eliminate.game.chat.action;

import com.thinkerwolf.eliminate.game.chat.service.IChatService;
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
        value = "chat@",
        views = {
                @View(name = "json", type = JsonView.class),
        })
@Api(tags = {"chat"}, description = "聊天相关接口")
public class ChatAction extends BaseAction {

    @Autowired
    IChatService chatService;

    @Command("sendText")
    @ApiOperation(value = "发送一条文字消息", notes = "发送一条文字消息")
    public JsonModel sendText(Request request, @RequestParam("text") String text) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(chatService.sendText(playerDto.getPlayerId(), text), request);
    }

    @Command("sendRed")
    @ApiOperation(value = "发送一个红包", notes = "发送一个红包")
    public JsonModel sendRed(Request request, @RequestParam("money") double money, @RequestParam("num") int num) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(chatService.sendRed(playerDto.getPlayerId(), money, num), request);
    }

    @Command("takeRed")
    @ApiOperation(value = "领取一个红包", notes = "领取一个红包")
    public JsonModel takeRed(Request request, @RequestParam("redId") String redId) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(chatService.takeRed(playerDto.getPlayerId(), redId), request);
    }

    @Command("getChats")
    @ApiOperation(value = "获取聊天记录列表", notes = "获取聊天记录列表")
    public JsonModel getChats(Request request, @RequestParam("page") int page) {
        PlayerDto playerDto = getPlayerDto(request);
        return createJsonModel(chatService.getChats(playerDto.getPlayerId(), page), request);
    }

}
