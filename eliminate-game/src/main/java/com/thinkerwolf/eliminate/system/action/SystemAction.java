package com.thinkerwolf.eliminate.system.action;

import com.thinkerwolf.eliminate.common.util.RequestUtils;
import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.system.service.ISystemService;
import com.thinkerwolf.gamer.core.annotation.Action;
import com.thinkerwolf.gamer.core.annotation.Command;
import com.thinkerwolf.gamer.core.annotation.RequestParam;
import com.thinkerwolf.gamer.core.annotation.View;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.mvc.view.HtmlView;
import com.thinkerwolf.gamer.core.mvc.view.JsonView;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Session;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Action(views = {
        @View(name = "byte", type = JsonView.class),
        @View(name = "freemarker", type = HtmlView.class),
        @View(name = "json", type = JsonView.class),
})
@Api(tags = "system", description = "系统相关接口")
public class SystemAction extends BaseAction {

    @Autowired
    private ISystemService systemService;

    @Value("${project.mapscript.enabled:true}")
    private boolean scriptEditEnabled;
    @Value("${project.docs.enabled:true}")
    private boolean docsEnabled;

    @Command("heartbeat")
    @ApiOperation(value = "心跳包")
    public JsonModel heartbeat(Request request) {
        PlayerDto playerDto = getPlayerDto(request);
        if (playerDto == null) {
            return null;
        }
        Session session = RequestUtils.getSession(request);
        if (session != null) {
            session.touch();
            return createJsonModel(OpResult.ok(), request);
        } else {
            return createJsonModel(OpResult.fail(LocalMessages.T_SESSION_1), request);
        }
    }

    @Command("saveScript")
    @ApiOperation(value = "保存地图json脚本")
    public JsonModel saveScript(Request request, @RequestParam("scriptId") int scriptId, @RequestParam("content") String content) {
        if (!scriptEditEnabled) {
            return createJsonModel(OpResult.fail("Edit mapScript closed"), request);
        }
        return createJsonModel(systemService.save(scriptId, content), request);
    }

    @Command("getScript")
    @ApiOperation(value = "获取地图json脚本")
    public JsonModel getScript(Request request, @RequestParam("scriptId") int scriptId) {
        if (!scriptEditEnabled) {
            return createJsonModel(OpResult.fail("Get mapScript closed"), request);
        }
        return createJsonModel(systemService.getScript(scriptId), request);
    }




}
