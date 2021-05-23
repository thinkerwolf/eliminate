package com.thinkerwolf.eliminate.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

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
 * @since 2020-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "player_id", type = IdType.AUTO)
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
     * game服务器
     */
    private String gameServer;


}
