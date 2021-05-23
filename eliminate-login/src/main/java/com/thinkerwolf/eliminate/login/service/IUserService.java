package com.thinkerwolf.eliminate.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thinkerwolf.eliminate.login.dto.LoginUserDto;
import com.thinkerwolf.eliminate.login.entity.User;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.gamer.core.servlet.Request;

public interface IUserService extends IService<User> {

    OpResult login(Request request, LoginUserDto loginUserDto);

    User getUser(String username);

}
