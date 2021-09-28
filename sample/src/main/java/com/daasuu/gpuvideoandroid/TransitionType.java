package com.daasuu.gpuvideoandroid;

import java.util.Arrays;
import java.util.List;

public enum TransitionType {
    TYPE_1,
    TYPE_2,
    TYPE_3,
    TYPE_4,
    TYPE_5,
    TYPE_6,
    TYPE_7,
    TYPE_8,
    TYPE_9,
    TYPE_10,
    TYPE_11,
    TYPE_12,
    TYPE_13,
    TYPE_14,
    TYPE_15,
    TYPE_16,
    TYPE_17,
    TYPE_18,
    TYPE_19;

    public static List<TransitionType> createTransitionList() {
        return Arrays.asList(TransitionType.values());
    }
}
