package com.thinkerwolf.eliminate.gateway.action;

import com.thinkerwolf.eliminate.gateway.service.IGatewayService;
import com.thinkerwolf.gamer.core.annotation.Action;
import com.thinkerwolf.gamer.core.annotation.Command;
import com.thinkerwolf.gamer.core.annotation.RequestParam;
import com.thinkerwolf.gamer.core.annotation.View;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.mvc.view.JsonView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Action(views = {
        @View(name = "json", type = JsonView.class)
})
public class GatewayAction {

    @Autowired
    IGatewayService gatewayService;

    @Command("gateway@testGameRpc")
    public JsonModel testGameRpc(@RequestParam("stype") String stype, @RequestParam("sid") String sid, @RequestParam("num") int num) {
        List<Integer> list = gatewayService.toGameTest(stype, sid, num);
        return new JsonModel(list);
    }

}
