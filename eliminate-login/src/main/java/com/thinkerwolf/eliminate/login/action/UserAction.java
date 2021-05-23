package com.thinkerwolf.eliminate.login.action;

import com.thinkerwolf.eliminate.login.dto.LoginUserDto;
import com.thinkerwolf.eliminate.login.service.IUserService;
import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.gamer.core.annotation.Action;
import com.thinkerwolf.gamer.core.annotation.Command;
import com.thinkerwolf.gamer.core.annotation.RequestParam;
import com.thinkerwolf.gamer.core.annotation.View;
import com.thinkerwolf.gamer.core.mvc.model.JacksonModel;
import com.thinkerwolf.gamer.core.mvc.view.JsonView;
import com.thinkerwolf.gamer.core.servlet.Request;
import org.springframework.beans.factory.annotation.Autowired;

@Action(
        views = {
                @View(name = "json", type = JsonView.class)
        }
)
public class UserAction extends BaseAction {

    @Autowired
    IUserService userService;

    @Command("user@login")
    public JacksonModel login(Request request, @RequestParam("username") String username, @RequestParam("password") String password) {
        LoginUserDto loginUserDto = LoginUserDto.builder().setUsername(username).setPassword(password)
                .setPlatform("tk")
                .setYx("tk")
                .setPic("1")
                .build();
        OpResult op = userService.login(request, loginUserDto);
        return createJsonModel(op, request);
    }
}
