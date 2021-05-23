package com.thinkerwolf.eliminate.pub.sdata.entity;

import java.io.Serializable;

import com.thinkerwolf.eliminate.pub.reward.RewardGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
public class DayGift implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 天数
     */
    private Integer day;

    /**
     * 进度
     */
    private Integer progress;

    /**
     * 进度图片
     */
    private String pic;

    /**
     * 奖励
     */
    private String reward;
}
