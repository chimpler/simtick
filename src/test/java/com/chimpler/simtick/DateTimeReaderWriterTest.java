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
        DateTime now = new DateTime();
        DateTime later = now.plusSeconds(2);
        writer.writeRaw(buffer, now, 1);
        writer.writeDelta(buffer, later, 15);
        System.out.printf("=======> " + reader.readRaw(buffer, 1));
        assert reader.readRaw(buffer, 1) == now;
        assert reader.readDelta(buffer, 15) == later;
    }
}
