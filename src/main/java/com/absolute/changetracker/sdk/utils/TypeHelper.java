package com.absolute.changetracker.sdk.utils;

import java.util.*;

public final class TypeHelper {
    private static final List<Class<?>> simpleTypes;

    static {
        simpleTypes = Arrays.asList(Boolean.class, Byte.class, Character.class, Date.class, Double.class, Enum.class, Float.class, Integer.class, String.class);
    }

    public static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() || simpleTypes.stream().anyMatch(t -> t == type);
    }
}
