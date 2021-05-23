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
 * @since 2020-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TalkPop implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer groupId;

    private Integer seq;

    private String intro;


}
