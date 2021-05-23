package com.thinkerwolf.eliminate.game.player.entity;

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
 * @since 2020-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "player_id", type = IdType.ASSIGN_ID)
    private Integer playerId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 玩家名称
     */
    private String playerName;

    /**
     * 玩家pic
     */
    private String pic;

    /**
     * 玩家体力
     */
    private Integer vit;

}
