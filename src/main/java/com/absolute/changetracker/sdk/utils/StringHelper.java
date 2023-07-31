package com.absolute.changetracker.sdk.utils;

import java.util.Objects;
import java.util.Optional;

public final class StringHelper {

    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty() || str.isBlank();
    }

    public static boolean safeEqualsIgnoreCase(String str1, String str2) {

        return Objects.equals(str1, str2) || Optional.ofNullable(str1).map(s -> s.equalsIgnoreCase(str2)).orElse(false);
    }
}
