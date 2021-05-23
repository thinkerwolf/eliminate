package com.thinkerwolf.eliminate.pub.battle.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.thinkerwolf.eliminate.pub.battle.Battle;
import com.thinkerwolf.eliminate.pub.battle.BattleConstants;
import com.thinkerwolf.eliminate.pub.battle.PropType;
import com.thinkerwolf.eliminate.pub.battle.event.EventPlayerOver;
import com.thinkerwolf.eliminate.pub.battle.map.IBlock;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfo;

import java.util.*;

/**
 * 战斗玩家
 *
 * @author wukai
 */
public class BattlePlayer {

    private final Battle battle;

    @JsonProperty(value = "blocks")
    private final Deque<IBlock> blocks = Queues.newLinkedBlockingDeque();

    private final Map<String, Integer> checkMap = Maps.newHashMap();

    private final PlayerRoleInfo roleInfo;

    private volatile PlayerState state;

    private final LinkedList<PropType> usedProps = Lists.newLinkedList();

    public BattlePlayer(Battle battle, PlayerRoleInfo roleInfo) {
        this.battle = battle;
        this.roleInfo = roleInfo;
        this.state = PlayerState.IDLE;
    }

    @JsonProperty(value = "playerId")
    public int getPlayerId() {
        return roleInfo.getPlayerId();
    }

    public Queue<IBlock> getBlocks() {
        return blocks;
    }

    public void addBlock(IBlock block) {
        if (!blocks.contains(block)) {
            blocks.add(block);
            checkMap.compute(block.getPic(), (s, num) -> num == null ? 1 : num + 1);
            checkBlocks();
        }
    }

    public void addBlocks(Collection<IBlock> blocks) {
        for (IBlock b : blocks) {
            blocks.add(b);
            checkMap.compute(b.getPic(), (s, num) -> num == null ? 1 : num + 1);
        }
        checkBlocks();
    }

    public void checkBlocks() {
        checkMap.forEach((s, num) -> {
            if (num >= BattleConstants.ERASURE_NUM) {
                checkMap.put(s, 0);
                blocks.removeIf(block -> s.equals(block.getPic()));
            }
        });
    }

    public IBlock popBlock() {
        IBlock block = blocks.pollLast();
        if (block != null) {
            checkMap.compute(block.getPic(), (s, num) -> num == null ? 0 : Math.max(0, num - 1));
        }
        return block;
    }

    public void checkAndOver() {
        if (isEmpty() && battle.getBattleMap().isEmpty()) {
            battle.getExecutor().addEvent(new EventPlayerOver(30, battle, getPlayerId(), true));
        } else if (getBlocks().size() >= BattleConstants.MAX_BOTTOM_NUM) {
            battle.getExecutor().addEvent(new EventPlayerOver(30, battle, getPlayerId(), false));
        }
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void addUsedProp(PropType pt) {
        usedProps.add(pt);
    }

    @JsonIgnore
    public LinkedList<PropType> getUsedProps() {
        return usedProps;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return blocks.isEmpty();
    }

    @JsonIgnore
    public boolean isLose() {
        return state == PlayerState.LOSE;
    }

    @JsonIgnore
    public boolean isOver() {
        return state == PlayerState.WIN || state == PlayerState.LOSE;
    }

    @JsonIgnore
    public boolean isWin() {
        return state == PlayerState.WIN;
    }

    @JsonIgnore
    public Map<String, Integer> getCheckMap() {
        return checkMap;
    }
}
