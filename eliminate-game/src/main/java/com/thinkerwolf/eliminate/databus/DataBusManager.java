package com.thinkerwolf.eliminate.databus;

public class DataBusManager {


    private static DataBus dataBus;

    public static void init(DataBus dataBus) {
        DataBusManager.dataBus = dataBus;
    }

    public static DataBus getDataBus() {
        return dataBus;
    }
}
