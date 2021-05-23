package com.thinkerwolf.eliminate.pub.sdata.entity;

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
 * @since 2020-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 建筑id
     */
    @TableId(type = IdType.INPUT)
    private Integer id;

    /**
     * 建筑名称
     */
    private String name;


}
