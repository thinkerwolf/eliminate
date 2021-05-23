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
 * @since 2020-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BuildingLv implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer buildingId;

    /**
     * 等级
     */
    private Integer lv;

    /**
     * 等级对应图片
     */
    private String pic;

    private String request;

    /**
     * 等级对应cd
     */
    private Integer cd;
    /**
     * 缩放
     */
    private Float scale;
}
