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
 * @since 2020-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Stage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关卡id
     */
    private Integer id;

    /**
     * 关卡名称
     */
    private String name;

    /**
     * 关卡对应的脚本id
     */
    private Integer scriptId;

    /**
     * 关卡奖励
     */
    private String reward;


}
