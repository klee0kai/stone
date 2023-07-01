package com.github.klee0kai.stone.utils;

public class LocalFieldName {

    private static long localVariableIndx = 0;


    public static String genLocalFieldName() {
        return "__lc" + localVariableIndx++;
    }


}
