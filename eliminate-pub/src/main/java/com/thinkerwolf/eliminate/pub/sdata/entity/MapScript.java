package com.thinkerwolf.eliminate.pub.sdata.entity;

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
 * @since 2020-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MapScript implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地图脚本
     */
    @TableId(value = "script_id", type = IdType.INPUT)
    private Integer scriptId;

    private String content;


}
