package com.thinkerwolf.eliminate.system.service;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.gamer.core.servlet.Request;

/**
 * 系统服务
 *
 * @author wukai
 */
public interface ISystemService {

    OpResult save(int scriptId, String content);

    OpResult getScript(int scriptId);


}
