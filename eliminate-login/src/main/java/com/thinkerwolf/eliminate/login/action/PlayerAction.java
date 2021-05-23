package com.thinkerwolf.eliminate.login.action;

import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.gamer.core.annotation.Action;
import com.thinkerwolf.gamer.core.annotation.View;
import com.thinkerwolf.gamer.core.mvc.view.JsonView;

@Action(
        views = {
                @View(name = "json", type = JsonView.class)
        }
)
public class PlayerAction extends BaseAction {


}
