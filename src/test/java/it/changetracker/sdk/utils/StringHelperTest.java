package it.changetracker.sdk.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringHelperTest {
    @Test
    public void testIsNullOrEmpty() {
        assertTrue(StringHelper.isNullOrEmpty(null));
        assertTrue(StringHelper.isNullOrEmpty(""));
        assertTrue(StringHelper.isNullOrEmpty("\n"));
        assertTrue(StringHelper.isNullOrEmpty("\t"));
        assertTrue(StringHelper.isNullOrEmpty(" "));
        assertTrue(StringHelper.isNullOrEmpty("   "));
        assertFalse(StringHelper.isNullOrEmpty("filled string"));
        assertFalse(StringHelper.isNullOrEmpty("123456"));
        assertFalse(StringHelper.isNullOrEmpty("string"));
    }
    @Test
    public void safeEqualsTest() {

        //null value checking
        assertTrue(StringHelper.safeEqualsIgnoreCase(null, null));
        assertFalse(StringHelper.safeEqualsIgnoreCase(null, "string2"));
        assertFalse(StringHelper.safeEqualsIgnoreCase("string1", null));

        //same content, same case
        assertTrue(StringHelper.safeEqualsIgnoreCase("", ""));
        assertTrue(StringHelper.safeEqualsIgnoreCase("string", "string"));
        assertTrue(StringHelper.safeEqualsIgnoreCase("String", "String"));
        assertTrue(StringHelper.safeEqualsIgnoreCase("STRING", "STRING"));
        assertTrue(StringHelper.safeEqualsIgnoreCase("  ", "  "));

        //same content, different case
        assertTrue(StringHelper.safeEqualsIgnoreCase("STRING", "string"));
        assertTrue(StringHelper.safeEqualsIgnoreCase("String", "string"));
        assertTrue(StringHelper.safeEqualsIgnoreCase("string", "STRING"));
        assertTrue(StringHelper.safeEqualsIgnoreCase("string", "String"));


        //different content, different case
        assertFalse(StringHelper.safeEqualsIgnoreCase("string 1", "string 2"));
        assertFalse(StringHelper.safeEqualsIgnoreCase("string 1", "String 2"));
        assertFalse(StringHelper.safeEqualsIgnoreCase("String 1", "String 2"));
        assertFalse(StringHelper.safeEqualsIgnoreCase("String 1", "string 2"));

        //object comparison
        var str1 = "";
        assertTrue(StringHelper.safeEqualsIgnoreCase(str1, str1));

        //empty vs null vs blank
        assertFalse(StringHelper.safeEqualsIgnoreCase("", null));
        assertFalse(StringHelper.safeEqualsIgnoreCase(null, ""));
        assertFalse(StringHelper.safeEqualsIgnoreCase(null, " "));
        assertFalse(StringHelper.safeEqualsIgnoreCase(" ", ""));
        assertFalse(StringHelper.safeEqualsIgnoreCase("", " "));

    }
}
