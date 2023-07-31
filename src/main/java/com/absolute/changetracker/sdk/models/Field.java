package com.absolute.changetracker.sdk.models;

import java.util.Objects;
import java.util.Optional;

public class Field {
    String name;
    String prevValue;
    String nextValue;
    Class<?> type;
    String format;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(String prevValue) {
        this.prevValue = prevValue;
    }

    public String getNextValue() {
        return nextValue;
    }

    public void setNextValue(String nextValue) {
        this.nextValue = nextValue;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return describe(RowStatus.Unknown);
    }

    public String describe(RowStatus rowStatus) {
        //check both null or equals comparison
        if (Objects.equals(prevValue, nextValue) || Optional.ofNullable(prevValue).map(pv -> pv.equalsIgnoreCase(nextValue)).orElse(false))
            return name + "=(" + prevValue + ")";

        var prev = Optional.ofNullable(prevValue).orElse("");
        var next = Optional.ofNullable(nextValue).orElse("");

        return switch (rowStatus) {
            case New -> "%s=(%s)".formatted(name, prev);
            case Deleted -> "%s=(%s)".formatted(name, next);
            default -> "%s=(%s) => %s)".formatted(name, prev, next);
        };
    }
}
