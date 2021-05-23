package com.thinkerwolf.eliminate.pub.listener;

import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.user.UserDto;
import com.thinkerwolf.eliminate.pub.user.Users;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.servlet.SessionAttributeEvent;
import com.thinkerwolf.gamer.core.servlet.SessionAttributeListener;

/**
 * Session属性设置监听器
 *
 * @author wukai
 */
public class LocalSessionAttributeListener implements SessionAttributeListener {

    private static final Logger LOG = InternalLoggerFactory.getLogger(LocalSessionAttributeListener.class);

    @Override
    public void attributeAdded(SessionAttributeEvent sae) {
        LOG.debug("Session attributeAdd {} {} {}", sae.getSource(), sae.getKey(), sae.getValue());
        if (sae.getValue() instanceof UserDto) {
            Users.addDto(sae.getSource(), (UserDto) sae.getValue());
        } else if (sae.getValue() instanceof PlayerDto) {
            Players.addDto(sae.getSource(), (PlayerDto) sae.getValue());
        }
    }

    @Override
    public void attributeRemoved(SessionAttributeEvent sae) {
        LOG.debug("Session attributeRemove {} {} {}", sae.getSource(), sae.getKey(), sae.getValue());
        if (sae.getValue() instanceof UserDto) {
            Users.removeDto(sae.getSource());
        } else if (sae.getValue() instanceof PlayerDto) {
            Players.removeDto(sae.getSource());
        }
    }
}
