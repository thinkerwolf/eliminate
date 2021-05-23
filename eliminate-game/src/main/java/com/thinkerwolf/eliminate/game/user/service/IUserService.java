package com.thinkerwolf.eliminate.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.game.user.entity.User;
import com.thinkerwolf.gamer.core.servlet.Request;

public interface IUserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param userId
     * @param password
     * @return
     */
    OpResult login(Request request, String userId, String password);

}
