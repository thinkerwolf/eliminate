package com.thinkerwolf.eliminate.common.util;

import com.google.common.collect.Lists;
import com.thinkerwolf.gamer.common.SymbolConstants;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public final class GameUtil {

    public static List<Integer> stringToInts(String str) {
        return stringToInts(str, SymbolConstants.COMMA);
    }

    public static List<Integer> stringToInts(String str, String split) {
        List<Integer> list = Lists.newArrayList();
        if (str == null || str.isEmpty()) {
            return list;
        }
        for (String s : str.split(split)) {
            if (!s.isEmpty()) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }


    public static String listToString(Collection coll, String split) {
        if (coll == null) {
            return null;
        }
        if (coll.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : coll) {
            sb.append(o.toString()).append(split);
        }
        return sb.substring(0, sb.length() - split.length());
    }

    public static String listToString(Collection coll) {
        return listToString(coll, SymbolConstants.COMMA);
    }

}
