package com.absolute.changetracker.sdk.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class ListHelper {
    public static <T> Optional<T> safeFindFirst(List<T> list, Predicate<? super T> predicate ) {
        return Optional.ofNullable(list).flatMap(l->l.stream().filter(predicate).findFirst());
    }
}
