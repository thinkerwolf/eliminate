package com.thinkerwolf.eliminate.game.user.action;

import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.eliminate.game.user.service.IUserService;
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
@Api(tags = {"user"}, description = "用户操作接口")
public class UserAction extends BaseAction {

    @Autowired
    IUserService userService;

    @Command("user@login")
    @ApiOperation(value = "玩家登录")
    public JsonModel login(Request request, @RequestParam("username") String username, @RequestParam("password") String password) {
        return createJsonModel(userService.login(request, username, password), request);
    }


}
