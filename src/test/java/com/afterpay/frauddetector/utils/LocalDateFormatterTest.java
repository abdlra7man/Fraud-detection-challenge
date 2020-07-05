package com.afterpay.frauddetector.utils;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class LocalDateFormatterTest {
    private static final String ISO_LOCAL_DATETIME = "2014-04-29T13:15:54";

    @Test
    public void testExecuteWithDefaultDateTimeFormat(){
        LocalDateFormatter localDateFormatter = new LocalDateFormatter();
        LocalDateTime localDateTime = localDateFormatter.execute(ISO_LOCAL_DATETIME);
        assertEquals(29, localDateTime.getDayOfMonth());
        assertEquals(Month.APRIL, localDateTime.getMonth());
        assertEquals(2014, localDateTime.getYear());
        assertEquals(ISO_LOCAL_DATETIME, localDateTime.toString());
    }

    @Test
    public void testRevertWithDefaultDateTimeFormat(){
        LocalDateFormatter localDateFormatter = new LocalDateFormatter();
        LocalDateTime localDateTime = LocalDateTime.parse(ISO_LOCAL_DATETIME);
        String localDateTimeStr = localDateFormatter.revert(localDateTime);
        assertEquals(localDateTime.toString(), localDateTimeStr);
    }

    @Test
    public void testExecuteWithNonDefaultDateTimeFormat(){
        LocalDateFormatter localDateFormatter = new LocalDateFormatter("dd-MM-yyyy HH:mm:ss");
        LocalDateTime localDateTime = localDateFormatter.execute("15-09-2020 12:09:22");
        assertEquals(15, localDateTime.getDayOfMonth());
        assertEquals(Month.SEPTEMBER, localDateTime.getMonth());
        assertEquals(2020, localDateTime.getYear());
    }

    @Test
    public void testRevertWithNonDefaultDateTimeFormat(){
        LocalDateFormatter localDateFormatter = new LocalDateFormatter("dd-MM-yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2020-09-15T12:09:22");
        String customDateTime = localDateFormatter.revert(localDateTime);
        assertEquals("15-09-2020 12:09:22", customDateTime);
    }
}
