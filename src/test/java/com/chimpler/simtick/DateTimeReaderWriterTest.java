package com.chimpler.simtick;

import com.chimpler.simtick.readers.DateTimeReader;
import com.chimpler.simtick.readers.ValueAndLength;
import com.chimpler.simtick.writers.DateTimeWriter;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateTimeReaderWriterTest {
    @Test
    public void testDateTimeReaderWriter() {
        byte[] buffer = new byte[100];
        DateTime minDate = new DateTime(2000, 1, 1, 0, 0, 0);
        DateTime maxDate = new DateTime(2020, 1, 1, 0, 0, 0);
        DateTimeReader reader = new DateTimeReader(minDate, maxDate, -20, 20, false);
        DateTimeWriter writer = new DateTimeWriter(minDate, maxDate, -20, 20, false);
        DateTime now = new DateTime().withMillisOfSecond(0);
        DateTime later = now.plusSeconds(2);
        writer.writeRaw(buffer, now, 1);
        writer.writeDelta(buffer, later, 33);
        assertEquals(new ValueAndLength<DateTime>(now, 30), reader.readRaw(buffer, 1));
        assertEquals(new ValueAndLength<DateTime>(later, 6), reader.readDelta(buffer, 33));
    }
}
