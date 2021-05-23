package com.thinkerwolf.eliminate.pub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thinkerwolf.eliminate.pub.reward.RewardGroup;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CommonTests {

    @Test
    public void testMap() {
        Map<String, Object> map = new HashMap<>(1, 1);
        map.put("dss", 1);
    }

    @Test
    public void testMath() {
        double d = 1.5d;
        System.out.printf("Ceil: %f -> %f\n", d, Math.ceil(d));
        d = 1.4d;
        System.out.printf("Ceil: %f -> %f\n", d, Math.ceil(d));
        d = -0.5d;
        System.out.printf("Ceil: %f -> %f\n", d, Math.ceil(d));
        d = 1.0d;
        System.out.printf("Ceil: %f -> %f\n", d, Math.ceil(d));

        d = 1.6d;
        System.out.printf("Floor: %f -> %f\n", d, Math.floor(d));
        d = 1.4d;
        System.out.printf("Floor: %f -> %f\n", d, Math.floor(d));
        d = -0.5d;
        System.out.printf("Floor: %f -> %f\n", d, Math.floor(d));


    }

    @Test
    public void testPath() {
        String parentPath = "C:/user/wukai";
        String fileName = "C:/user/wukai/doc/cf.md";
        String s = fileName.substring(parentPath.length());
        int i = s.indexOf("cf.md");
        System.out.println(s.substring(0, i - 1));
    }

    @Test
    public void testReward() {
        RewardGroup rewardGroup = RewardGroup.group("building_lv:1,1;building:1;star:1");
        try {
            String json = JsonModel.objectMapper.writeValueAsString(rewardGroup);
            System.err.println(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println(System.getenv("JAVA_HOME"));
        System.out.println(System.getProperty("JAVA_HOME"));
        System.out.println(System.getenv("gamer.test"));
        System.out.println(System.getProperty("gamer.test"));
    }
}
