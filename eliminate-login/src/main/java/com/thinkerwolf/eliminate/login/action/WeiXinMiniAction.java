package com.thinkerwolf.eliminate.login.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.util.HttpUtils;
import com.thinkerwolf.eliminate.login.dto.LoginUserDto;
import com.thinkerwolf.eliminate.login.service.IUserService;
import com.thinkerwolf.eliminate.pub.common.BaseAction;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.annotation.Action;
import com.thinkerwolf.gamer.core.annotation.Command;
import com.thinkerwolf.gamer.core.annotation.RequestParam;
import com.thinkerwolf.gamer.core.annotation.View;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.mvc.view.JsonView;
import com.thinkerwolf.gamer.core.servlet.Request;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信接口
 *
 * @author wukai
 */
@Action(views = {
        @View(name = "json", type = JsonView.class)
})
public class WeiXinMiniAction extends BaseAction {

    private static final Logger LOG = InternalLoggerFactory.getLogger(WeiXinMiniAction.class);
    /**
     * 微信小游戏平台
     */
    public static final String PLATFORM = "wx-mini";
    /**
     * 运营商
     */
    public static final String YX = "wx-mini";

    public static final String USERNAME_FORMAT = "wx-mini_%s";

    public static final String AppID = "AppID";
    public static final String Secret = "SECRET";
    /**
     * 微信登录
     */
    public static final String AuthCode2Session = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Autowired
    IUserService userService;

    @Command("wxMiniLogin")
    public JsonModel wxLogin(Request request, @RequestParam("code") String code) {
        String address = String.format(AuthCode2Session, AppID, Secret, code);
        try {
            byte[] bs = HttpUtils.sendGetRequest(address);
            JsonNode jsonNode = JsonModel.objectMapper.readTree(bs);
            int errcode = jsonNode.get("errcode").asInt();
            if (errcode != 0) {
                String errmsg = jsonNode.get("errmsg").asText();
                return createJsonModel(OpResult.fail(errmsg), request);
            }
            String openid = jsonNode.get("openid").asText();
            String username = String.format(USERNAME_FORMAT, openid);
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .setUsername(username)
                    .setPassword("")
                    .setPlatform(PLATFORM)
                    .setYx(YX)
                    .setPic("1")
                    .build();
            OpResult op = userService.login(request, loginUserDto);
            return createJsonModel(op, request);
        } catch (Exception e) {
            LOG.error("wx-mini login", e);
            return createJsonModel(OpResult.fail("Login error"), request);
        }

    }

}