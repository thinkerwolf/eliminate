package com.thinkerwolf.eliminate.pub.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateUtil {


    public static long getRemain(Date lastDate, long totalMillis, TimeUnit remainUnit) {
        long pass = System.currentTimeMillis() - lastDate.getTime();
        if (pass < 0) {
            pass = 0;
        }
        return remainUnit.convert(totalMillis - pass, TimeUnit.MILLISECONDS);
    }

    public static long getRemain(long lastMillis, long totalMillis, TimeUnit remainUnit) {
        long pass = System.currentTimeMillis() - lastMillis;
        if (pass < 0) {
            pass = 0;
        }
        return remainUnit.convert(totalMillis - pass, TimeUnit.MILLISECONDS);
    }

    public static Date addTime(Date date, long time, TimeUnit unit) {
        return new Date(date.getTime() + unit.toMillis(time));
    }

    public static Date reduceTime(Date date, long time, TimeUnit unit) {
        return new Date(date.getTime() - unit.toMillis(time));
    }

}
