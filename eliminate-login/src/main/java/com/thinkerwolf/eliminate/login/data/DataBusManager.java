package com.thinkerwolf.eliminate.login.data;

public class DataBusManager {


    private static DataBus dataBus;

    public static void init(DataBus dataBus) {
        DataBusManager.dataBus = dataBus;
    }

    public static DataBus getDataBus() {
        return dataBus;
    }
}
