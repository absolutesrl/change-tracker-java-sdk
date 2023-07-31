package com.absolute.changetracker.sdk.core;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class FormatterTest {
    @Test
    void formatterTest() {
        //decimal formatter
        assertEquals(FieldMapper.convertValue(10.623f, "0.00"), "10.62");
        assertEquals(FieldMapper.convertValue(10.6f, "0.00"), "10.60");
        assertEquals(FieldMapper.convertValue(10.623, "0.00"), "10.62");
        assertEquals(FieldMapper.convertValue(10.6, "0.00"), "10.60");
        assertEquals(FieldMapper.convertValue(10.6, ""), "10.6");

        //string formatter
        assertEquals(FieldMapper.convertValue("text"), "text");
        assertEquals(FieldMapper.convertValue(true), "true");
        assertEquals(FieldMapper.convertValue(100, "0.00"), "0.00");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);

        String dateInString = "07-07-2023 00:00:00";
        try {
            Date date = formatter.parse(dateInString);
            assertEquals(FieldMapper.convertValue(date), "2023-07-07T00:00:00.000+02:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
