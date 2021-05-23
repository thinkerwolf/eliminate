package com.thinkerwolf.eliminate.pub.common;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.util.RequestUtils;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.user.UserDto;
import com.thinkerwolf.eliminate.pub.user.Users;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Session;

public abstract class BaseAction {

    protected PlayerDto getPlayerDto(Request request) {
        Session session = RequestUtils.getSession(request);
        return Players.getDto(session);
    }

    protected UserDto getUserDto(Request request) {
        Session session = request.getSession(false);
        return Users.getDto(session);
    }

    protected JsonModel createJsonModel(OpResult op, Request request) {
        op.setRequestId(request.getRequestId());
        return new JsonModel(op);
    }

}
