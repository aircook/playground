package com.tistory.aircook.playground.config.database;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class QueryFunction {

    // SQL 쿼리 if 엘리먼트에서 사용할 empty 함수
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).isEmpty();
        } else if (obj instanceof List) {
            return ((List) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        } else if (obj instanceof Object[]) {
            return Array.getLength(obj) == 0;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isInList(Object obj, List<Object> list) {
        return list.contains(obj);
    }

    public static String escapeLike(String query) {
        return escapeLike(query, true, true);
    }

    public static String escapeLike(String query , boolean pre, boolean post) {
        return " LIKE '%' || #{" + query +"} || '%' ESCAPE '@' ";
    }

    public static String[] toStringArray(String str) {
        String[] arr = {};
        if (str != null && str.length() > 2) {
            str = str.substring(1, str.length() - 1);
            arr = str.split(", ");
            System.out.println(str);
        }
        return arr;
    }
}
