package com.thinkerwolf.eliminate.bag.entity;

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
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlayerBag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**
     * 玩家id
     */
    private Integer playerId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 物品id
     */
    private Integer detailId;

    /**
     * 数量
     */
    private Integer num;


}
