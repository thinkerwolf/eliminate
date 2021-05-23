package com.thinkerwolf.eliminate;


import com.thinkerwolf.eliminate.game.chat.entity.Chat;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RedTests {


    @Test
    public void testRed() {
        double money = 120.0;
        int num = 30;
        Chat chat = new Chat();
        chat.setToMoney(money);
        chat.setToNum(num);
        chat.setCurMoney(money);
        chat.setCurNum(num);
        List<Double> mlist = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            double m = getRandomMoney(chat);
            mlist.add(m);
            System.out.print(m + ", ");
        }
        double total = 0;
        for (double d : mlist) {
            total += d;
        }
        System.out.println("\n Send " + total);
    }

    public static double getRandomMoney(Chat chat) {
        final int curNum = chat.getCurNum();
        final double curMoney = chat.getCurMoney();
        if (curNum == 1) {
            chat.setCurNum(0);
            chat.setCurMoney(0);
            return (double) Math.round(curMoney * 100) / 100;
        }
        double min = 0.01;
        double max = curMoney / curNum * 2;
        double money = RandomUtils.nextDouble() * max;
        money = money <= min ? 0.01D : money;
        money = Math.floor(money * 100) / 100;
        chat.setCurNum(curNum - 1);
        chat.setCurMoney(curMoney - money);
        return money;
    }


}
