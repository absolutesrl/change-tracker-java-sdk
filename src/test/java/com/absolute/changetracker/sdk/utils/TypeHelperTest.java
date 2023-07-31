package com.absolute.changetracker.sdk.utils;

import com.absolute.changetracker.sdk.testmodels.TestModel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TypeHelperTest {

    @Test
    void testIsSimpleType() {
        //primitive types
        assertTrue(TypeHelper.isSimpleType(char.class));
        assertTrue(TypeHelper.isSimpleType(int.class));
        assertTrue(TypeHelper.isSimpleType(byte.class));
        assertTrue(TypeHelper.isSimpleType(boolean.class));
        assertTrue(TypeHelper.isSimpleType(float.class));
        assertTrue(TypeHelper.isSimpleType(double.class));

        //primitive types wrapper
        assertTrue(TypeHelper.isSimpleType(Character.class));
        assertTrue(TypeHelper.isSimpleType(String.class));
        assertTrue(TypeHelper.isSimpleType(Integer.class));
        assertTrue(TypeHelper.isSimpleType(Byte.class));
        assertTrue(TypeHelper.isSimpleType(Boolean.class));
        assertTrue(TypeHelper.isSimpleType(Float.class));
        assertTrue(TypeHelper.isSimpleType(Double.class));
        assertTrue(TypeHelper.isSimpleType(Enum.class));

        //other
        assertFalse(TypeHelper.isSimpleType(Object.class));
        assertFalse(TypeHelper.isSimpleType(List.class));
        assertFalse(TypeHelper.isSimpleType(Set.class));
        assertFalse(TypeHelper.isSimpleType(Map.class));
        assertFalse(TypeHelper.isSimpleType(TestModel.class));
    }
}
