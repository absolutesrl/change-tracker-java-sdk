package it.changetracker.sdk.core;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TypeToStringFormatter {
    public static String format(Date value, String format) {
        if (value == null) return "";

        var sdf = new SimpleDateFormat(getFormatOrDefault(format, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));

        return sdf.format(value);
    }

    public static String format(Boolean value, String format) {
        if (value == null) return "";
        return String.valueOf(value);
    }

    public static String format(String value, String format) {
        if (value == null) return "";
        return value;
    }

    public static String format(Float value, String format) {
        if (value == null) return "";
        return getDecimalFormatter(format).format(value);
    }

    public static String format(Double value, String format) {
        if (value == null) return "";
        return getDecimalFormatter(format).format(value);
    }

    public static String format(Integer value, String format) {
        if (value == null) return "";
        return String.format(Locale.ENGLISH, getFormatOrDefault(format, "%d"), value);
    }

    private static String getFormatOrDefault(String format, String defaultFormat) {
        if (format == null) return defaultFormat;
        return format;
    }

    private static DecimalFormat getDecimalFormatter(String format) {
        var symbols = new DecimalFormatSymbols(Locale.US);
        return new DecimalFormat(getFormatOrDefault(format, "0.####"), symbols);
    }

    public static <T> String format(T value, String format) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (value == null) return "";
        return (String) TypeToStringFormatter.class.getMethod("format", value.getClass(), String.class)
                .invoke(null, value, format);
    }
}
