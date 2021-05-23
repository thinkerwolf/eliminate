package com.thinkerwolf.eliminate.game.building.entity;

import java.io.Serializable;
import java.util.Date;

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
public class PlayerBuilding implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private Integer playerId;

    private Integer buildingId;

    private Integer lv;

    private Integer state;

    private Date updateTime;


}
