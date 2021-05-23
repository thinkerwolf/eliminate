package com.thinkerwolf.eliminate.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class CommonTests {

    private static final Logger log = InternalLoggerFactory.getLogger(CommonTests.class);

    @Test
    public void testFastjson() {
        byte[] data = new byte[]{1, 2};
        Map<String, Object> vars = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode obn = (ObjectNode) mapper.readTree(data);
            JsonNode dn = obn.get("data");
            JsonNode pln = dn.get("playerList");
            int playerId = pln.get(0).get("playerId").asInt();
            vars.put("playerId", playerId);
        } catch (Exception e) {
            log.error("", e);
        }

    }

}
