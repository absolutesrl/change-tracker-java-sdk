package com.absolute.changetracker.sdk.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListHelperTest {
    @Test
    void safeFindFirst() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);

        assertEquals(ListHelper.safeFindFirst(list, el->true).orElse(null), 1);
        assertNotEquals(ListHelper.safeFindFirst(null, el->true).orElse(null), 1);

        assertNull(ListHelper.safeFindFirst(list, el -> false).orElse(null));
        assertNull(ListHelper.safeFindFirst(list, el -> el == 7).orElse(null));

    }
}
