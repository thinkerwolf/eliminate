package com.thinkerwolf.eliminate.pub.sdata.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wukai
 * @since 2020-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CharacterAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 行为组id
     */
    private Integer groupId;

    /**
     * 行为触发点
     */
    private String point;

    /**
     * 动作资源
     */
    private String action;

    /**
     * 对话组id
     */
    private Integer talkGid;

    /**
     * 对话播放类型
     */
    private Integer talkType;

    /**
     * 对话气泡组
     */
    private Integer talkPopGid;

    /**
     * 对话气泡播放类型
     */
    private Integer talkPopType;


}
