package com.alading.util;

import com.alading.tool.stock.Kline;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtil {

    public static List<Kline> slice(List<Kline> source, int start, int end) {
        List<Kline> list = new ArrayList<>();
        for (int i = start; i < end; i++) {
            list.add(source.get(i));
        }
        return list;
    }
}
