package com.softwaremagico.tm.advisor.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {
    private TimeZone originalTimeZone;

    @Before
    public void setUp() {
        originalTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @After
    public void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }

    @Test
    public void formatTimestampReturnsEmptyStringForNull() {
        assertEquals("", DateUtils.formatTimestamp(null));
    }

    @Test
    public void formatTimestampFormatsKnownTimestamp() {
        assertEquals("1970-01-01 12:00:00", DateUtils.formatTimestamp(new Timestamp(0L)));
    }
}
