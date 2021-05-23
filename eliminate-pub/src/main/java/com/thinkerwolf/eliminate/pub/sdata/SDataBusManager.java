package com.thinkerwolf.eliminate.pub.sdata;

public class SDataBusManager {

    private static SDataBus sDataBus;

    public static void init(SDataBus sDataBus) {
        SDataBusManager.sDataBus = sDataBus;
    }

    public static SDataBus getDataGetter() {
        return sDataBus;
    }
}
