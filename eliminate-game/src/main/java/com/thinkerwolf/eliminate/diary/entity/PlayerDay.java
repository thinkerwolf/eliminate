package com.thinkerwolf.eliminate.diary.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wukai
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlayerDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 玩家id
     */
    private Integer playerId;

    /**
     * 天数
     */
    private Integer day;

    /**
     * 当前进度
     */
    private Integer currentProcess;

    /**
     * 下一次进度
     */
    private Integer nextProcess;

    /**
     * 已领取奖励索引
     */
    private String takes;
}
