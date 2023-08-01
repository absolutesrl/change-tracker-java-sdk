package it.changetracker.sdk.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public final class FieldMapper {
    public static <T> String convertValue(T value) {
        return convertValue(value, null);
    }
    public static <T> String convertValue(T value, String format) {
        if (value == null) return "";

        String res;

        try {
            res = TypeToStringFormatter.format(value, format);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return String.valueOf(value);
        } catch (Exception e) {
            return convertValue(value, null);
        }

        if (res == null)
            res = String.valueOf(value);

        return Optional.ofNullable(res).orElse("");
    }
}
