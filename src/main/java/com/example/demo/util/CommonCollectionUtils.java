package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;

public class CommonCollectionUtils {

    private CommonCollectionUtils() {
        throw new AssertionError();
    }

    /**
     * 避免泛型冗长，精简代码
     * eg: HashMap<String,Map<String,Object>>
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMapInstance() {
        return new HashMap<K, V>();
    }

    public static void main(String[] args) {
        Map<String, Map<String, Map<String, Object>>> map1 = new HashMap<>();

        Map<String, Map<String, Map<String, Object>>> map2 = CommonCollectionUtils.newHashMapInstance();
    }
}
