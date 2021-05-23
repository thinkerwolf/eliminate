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
 * @since 2020-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private Integer id;

    /**
     * 是否是开始任务
     */
    private Integer begin;

    /**
     * 下一个任务
     */
    private String next;

    /**
     * 天数
     */
    private Integer day;

    /**
     * 时间段
     */
    private Integer time;

    /**
     * 任务对应关卡id
     */
    private String stageId;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 描述
     */
    private String intro;

    /**
     * 任务完成后的奖励
     */
    private String reward;
    /**
     * 任务完成消耗
     */
    private Integer cost;
    /**
     * 任务图片
     */
    private String pic;
    /**
     * 任务完成角色行为actionGid
     */
    private Integer characterActionGid;
}
