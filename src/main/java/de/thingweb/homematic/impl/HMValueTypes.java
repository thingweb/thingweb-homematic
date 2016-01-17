package de.thingweb.homematic.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johannes on 12.12.2015.
 */
public enum HMValueTypes {
    BOOLEAN,
    FLOAT,
    INTEGER,
    ACTION,
    UNKNOWN;

    private static final Map<Integer,HMValueTypes> codeMap = new HashMap<>();
    static {
        codeMap.put(2,BOOLEAN);
        codeMap.put(4,FLOAT);
        codeMap.put(16, INTEGER);
        codeMap.put(8, INTEGER);
        codeMap.put(20, ACTION);
    }

    public static HMValueTypes resolve(int code) {
        return codeMap.containsKey(code)? codeMap.get(code) : UNKNOWN;
    }
}
