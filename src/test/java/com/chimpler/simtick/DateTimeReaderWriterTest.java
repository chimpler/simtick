package com.chimpler.simtick;

import com.chimpler.simtick.readers.DateTimeReader;
import com.chimpler.simtick.readers.LongReader;
import com.chimpler.simtick.writers.DateTimeWriter;
import com.chimpler.simtick.writers.LongWriter;
import org.joda.time.DateTime;
import org.junit.Test;

public class DateTimeReaderWriterTest {
    @Test
    public void testDateTimeReaderWriter() {
        byte[] buffer = new byte[100];
        DateTimeReader reader = new DateTimeReader(10, false);
        DateTimeWriter writer = new DateTimeWriter(10, false);
        DateTime now = new DateTime().withMillisOfSecond(0);
        DateTime later = now.plusSeconds(2);
        writer.writeRaw(buffer, now, 1);
        writer.writeDelta(buffer, later, 33);
        assert now.equals(reader.readRaw(buffer, 1));
        assert later.equals(reader.readDelta(buffer, 33));
    }
}
