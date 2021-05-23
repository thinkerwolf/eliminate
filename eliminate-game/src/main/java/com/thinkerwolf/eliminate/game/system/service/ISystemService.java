package com.thinkerwolf.eliminate.game.system.service;

import com.thinkerwolf.eliminate.common.OpResult;

/**
 * 系统服务
 *
 * @author wukai
 */
public interface ISystemService {

    OpResult save(int scriptId, String content);

    OpResult getScript(int scriptId);


}
