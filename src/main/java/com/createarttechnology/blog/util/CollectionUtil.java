package com.createarttechnology.blog.util;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by lixuhui on 2018/9/18.
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Collection c) {
        return !isEmpty(c);
    }

    public static boolean isEmpty(Map m) {
        return m == null || m.isEmpty();

    }

    public static boolean isNotEmpty(Map m) {
        return !isEmpty(m);
    }

    public static <I, O> List<O> transformList(List<I> inputList, Function<I, O> function) {
        if (inputList == null)
            return null;

        List<O> outputList = Lists.newArrayListWithExpectedSize(inputList.size());
        if (inputList.size() == 0)
            return Lists.newArrayList();

        for (I inputItem : inputList) {
            if (inputItem != null) {
                O outputItem = function.apply(inputItem);
                if (outputItem != null) {
                    outputList.add(outputItem);
                }
            }
        }

        return outputList;
    }

}
